import re
import os
import matplotlib

# Forzar el backend TkAgg para compatibilidad multiplataforma (Windows/Linux)
matplotlib.use('TkAgg') 

import matplotlib.pyplot as plt
from matplotlib.widgets import CheckButtons, TextBox

# ==========================================
# CONFIGURATION
# ==========================================
TARGET_FILE = "output.txt"
MANUAL_COLORS = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6']
# ==========================================

def read_and_parse_file(filepath):
    """
    Reads log file, categorizes algorithms, AND extracts numerical parameters for advanced filtering.
    """
    experiments = []
    
    if not os.path.exists(filepath):
        print(f"\n[ERROR] File not found: {filepath}")
        return []

    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    blocks = content.split('-----------------------------------------------------------------------------')
    print(f"Found {len(blocks)} blocks. Processing...")

    for block in blocks:
        if not block.strip(): continue
        
        # --- 1. Basic Extraction ---
        mating_match = re.search(r'Mating Pool Operator:\s*(.*?)(?:\n|$)', block)
        cross_match = re.search(r'CrossOver Operator:\s*(.*?)(?:\n|$)', block)
        mut_match = re.search(r'Mutation Operator:\s*(.*?)(?:\n|$)', block)
        surv_match = re.search(r'Survivor Selection:\s*(.*?)(?:\n|$)', block)
        fitness_match = re.search(r'\[([\d,\s]+)\]', block)

        if not fitness_match: continue

        # Full Strings
        mating_full = mating_match.group(1).strip() if mating_match else "Unknown"
        cross_full = cross_match.group(1).strip() if cross_match else "Unknown"
        mut_full = mut_match.group(1).strip() if mut_match else "Unknown"
        surv_full = surv_match.group(1).strip() if surv_match else "Unknown"

        # --- 2. Filter Keys (Categories) ---
        keys = {
            'mating': mating_full.split(' ')[0],
            'crossover': cross_full.split(' Chance:')[0].strip(),
            'mutation': mut_full.split(' Chance:')[0].strip(),
            'survivor': surv_full.split(' n:')[0].split(' second')[0].strip()
        }

        # --- 3. Parameter Extraction (Numbers & Booleans) ---
        params = {}

        # Extract 'k' (Tournament size)
        k_match = re.search(r'k:\s*(\d+)', block)
        params['k'] = int(k_match.group(1)) if k_match else -1

        # Extract 'n' (Population/Pool size)
        n_match = re.search(r'n:\s*(\d+)', block)
        params['n'] = int(n_match.group(1)) if n_match else -1

        # Extract 'chance' (Crossover)
        c_chance_match = re.search(r'CrossOver.*?Chance:\s*([\d\.]+)', block)
        params['cx'] = float(c_chance_match.group(1)) if c_chance_match else 0.0

        # Extract 'chance' (Mutation)
        m_chance_match = re.search(r'Mutation.*?Chance:\s*([\d\.]+)', block)
        params['mx'] = float(m_chance_match.group(1)) if m_chance_match else 0.0

        # Extract 'replacement' (Boolean)
        rep_match = re.search(r'withReplacement:\s*(true|false)', block, re.IGNORECASE)
        if rep_match:
            params['rep'] = True if rep_match.group(1).lower() == 'true' else False
        else:
            params['rep'] = None

        # Extract Final Cost
        try:
            fitness_values = [int(x) for x in fitness_match.group(1).replace('\n', '').split(', ')]
            final_score = fitness_values[-1]
            params['cost'] = final_score
        except ValueError:
            continue 

        # Build Experiment Object
        label_text = (f"Cost: {final_score}\n"
                      f"M: {mating_full}\n"
                      f"C: {cross_full}\n"
                      f"Mut: {mut_full}")

        experiments.append({
            'history': fitness_values,
            'final_score': final_score,
            'label': label_text,
            'keys': keys,
            'params': params # Dictionary of numerical values
        })

    return experiments

def check_condition(exp_params, query):
    """
    Evaluates a user query string against the experiment parameters.
    """
    query = query.strip().lower()
    if not query: return True

    var_map = {
        'k': 'k', 'n': 'n', 'S': 'S',
        'c': 'cx', 'cx': 'cx', 'cross': 'cx', 
        'm': 'mx', 'mx': 'mx', 'mut': 'mx',   
        'rep': 'rep', 'replacement': 'rep',   
        'cost': 'cost'
    }

    try:
        match = re.match(r'([a-z]+)\s*(>=|<=|==|=|!=|>|<)\s*(.+)', query)
        if not match: return True 

        user_var, op, user_val = match.groups()
        
        if user_var not in var_map: return True 
        actual_val = exp_params.get(var_map[user_var])
        if actual_val is None: return False 

        target_val = None
        if user_val in ['true', 't']: target_val = True
        elif user_val in ['false', 'f']: target_val = False
        else:
            try: target_val = float(user_val)
            except: return True 

        if op == '=': op = '==' 
        if op == '>': return actual_val > target_val
        if op == '<': return actual_val < target_val
        if op == '>=': return actual_val >= target_val
        if op == '<=': return actual_val <= target_val
        if op == '==' or op == '=': return actual_val == target_val
        if op == '!=': return actual_val != target_val

    except Exception:
        return True
    
    return True

def generate_dashboard(experiments):
    if not experiments:
        print("No valid data found.")
        return

    plt.style.use('seaborn-v0_8-whitegrid')
    
    # Increase bottom margin for controls (Checkbox + TextBox)
    fig, ax = plt.subplots(figsize=(16, 11))
    plt.subplots_adjust(bottom=0.40) 

    # --- Setup Lines ---
    unique_keys = {
        'mating': sorted(list(set(e['keys']['mating'] for e in experiments))),
        'crossover': sorted(list(set(e['keys']['crossover'] for e in experiments))),
        'mutation': sorted(list(set(e['keys']['mutation'] for e in experiments))),
        'survivor': sorted(list(set(e['keys']['survivor'] for e in experiments)))
    }

    lines = []
    for i, exp in enumerate(experiments):
        col = MANUAL_COLORS[i % len(MANUAL_COLORS)]
        line, = ax.plot(exp['history'], alpha=0.5, linewidth=1.5, color=col, picker=True, pickradius=5, label=exp['label'])
        line.data_source = exp 
        lines.append(line)

    ax.set_xlabel("Iterations", fontsize=11, fontweight='bold')
    ax.set_ylabel("Fitness Cost", fontsize=11, fontweight='bold')
    
    # --- HEADER TITLES ---
    # We use two separate text objects: one for the count, one for the Average
    title_count = ax.text(0.5, 1.02, f"Visible: {len(experiments)} Runs", 
                          transform=ax.transAxes, ha='center', fontsize=12, fontweight='bold', color='#333333')
    
    # Initial Average Calculation
    initial_avg = sum(e['final_score'] for e in experiments) / len(experiments)
    title_avg = ax.text(0.5, 1.055, f"Avg Best Fitness: {initial_avg:.2f}", 
                        transform=ax.transAxes, ha='center', fontsize=14, fontweight='bold', color='#e6194b')

    # ==========================================
    # 1. CHECKBOXES (Categorical)
    # ==========================================
    active_filters = {} 
    
    def create_checkbox(x, title, cats):
        ax_c = plt.axes([x, 0.12, 0.20, 0.20], frameon=False) 
        ax_c.set_title(title, fontsize=10, fontweight='bold', loc='left')
        check = CheckButtons(ax_c, cats, [True]*len(cats))
        try:
             if hasattr(check, 'rectangles'):
                 for r in check.rectangles: r.set_facecolor('#dddddd')
        except: pass
        for t in check.labels: t.set_fontsize(9)
        active_filters[title] = set(cats)
        return check

    check_m = create_checkbox(0.05, "Mating", unique_keys['mating'])
    check_c = create_checkbox(0.28, "Crossover", unique_keys['crossover'])
    check_mut = create_checkbox(0.51, "Mutation", unique_keys['mutation'])
    check_s = create_checkbox(0.74, "Survivor", unique_keys['survivor'])

    # ==========================================
    # 2. TEXT BOX (Parameter Rules)
    # ==========================================
    axbox = plt.axes([0.20, 0.02, 0.60, 0.04]) 
    text_box = TextBox(axbox, 'Param Filter: ', initial="", color='#f0f0f0', hovercolor='#ffffff')
    text_box.label.set_fontweight('bold')
    
    fig.text(0.50, 0.07, "Examples: k > 5 | rep = true | cx >= 0.8 | cost < 5650", 
             ha='center', fontsize=9, color='#555555', style='italic')

    # ==========================================
    # 3. UPDATE LOGIC (Includes Avg Calculation)
    # ==========================================
    def update_graph(val=None):
        # 1. Get Checkbox States
        def get_active_cats(widget, cats):
            return {cat for cat, on in zip(cats, widget.get_status()) if on}

        act_m = get_active_cats(check_m, unique_keys['mating'])
        act_c = get_active_cats(check_c, unique_keys['crossover'])
        act_mut = get_active_cats(check_mut, unique_keys['mutation'])
        act_s = get_active_cats(check_s, unique_keys['survivor'])

        query = text_box.text
        visible_scores = []
        visible_count = 0
        
        for line in lines:
            exp = line.data_source
            keys = exp['keys']
            params = exp['params']

            # Check Categories 
            cat_match = (keys['mating'] in act_m and 
                         keys['crossover'] in act_c and 
                         keys['mutation'] in act_mut and 
                         keys['survivor'] in act_s)
            
            # Check Parameters 
            param_match = check_condition(params, query)

            is_visible = cat_match and param_match
            
            line.set_visible(is_visible)
            if is_visible:
                line.set_alpha(0.5)
                visible_count += 1
                visible_scores.append(exp['final_score'])

        # --- Update Header Stats ---
        if visible_count > 0:
            avg_fitness = sum(visible_scores) / visible_count
            title_avg.set_text(f"Avg Best Fitness: {avg_fitness:.2f}")
            title_count.set_text(f"Visible: {visible_count} Runs")
            ax.relim()
            ax.autoscale_view()
        else:
            title_avg.set_text("Avg Best Fitness: N/A")
            title_count.set_text("Visible: 0 Runs")

        fig.canvas.draw_idle()

    # Wire events
    check_m.on_clicked(update_graph)
    check_c.on_clicked(update_graph)
    check_mut.on_clicked(update_graph)
    check_s.on_clicked(update_graph)
    text_box.on_submit(update_graph)

    # Hover logic 
    def on_move(event):
        if event.inaxes != ax: return
        highlighted = False
        for line in lines:
            if not line.get_visible(): continue 
            contains, _ = line.contains(event)
            if contains:
                line.set_alpha(1.0); line.set_linewidth(3.0); line.set_zorder(10)
                # Note: We don't update the MAIN titles on hover anymore to keep the stats visible
                # You could add a temporary text if you wanted, but usually color highlight is enough
                highlighted = True
            else:
                line.set_alpha(0.1); line.set_linewidth(1); line.set_zorder(1)
        if not highlighted:
            for line in lines:
                if line.get_visible(): line.set_alpha(0.5)
        fig.canvas.draw_idle()

    fig.canvas.mpl_connect('motion_notify_event', on_move)
    print("Dashboard ready.")
    plt.show()

if __name__ == "__main__":
    script_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(script_dir, TARGET_FILE)
    print(f"Reading: {file_path}")
    data = read_and_parse_file(file_path)
    generate_dashboard(data)