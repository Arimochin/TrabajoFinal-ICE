import re
import os
import matplotlib.pyplot as plt
from matplotlib.widgets import TextBox
from matplotlib.lines import Line2D

# ==========================================
# CONFIGURATION
# ==========================================
TARGET_FILE = "output.txt"
MANUAL_COLORS = ['red', 'blue', 'green', 'black']
# ==========================================

def read_and_parse_file(filepath):
    """
    Reads the log, extracting data and a unique configuration summary per group.
    """
    experiments = []
    group_configs = {} # Dictionary to store one config string per group ID
    
    if not os.path.exists(filepath):
        print(f"\n[ERROR] File not found: {filepath}")
        return [], {}

    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    blocks = content.split('-----------------------------------------------------------------------------')
    print(f"Found {len(blocks)} blocks. Processing...")

    for block in blocks:
        if not block.strip(): continue
        
        # --- Regex Extraction ---
        group_match = re.search(r'Group:\s*(\d+)', block)
        group_id = int(group_match.group(1)) if group_match else 0
        
        survivor_match = re.search(r'Survivor Selection:\s*(.*?)\n', block)
        mating_match = re.search(r'Mating Pool Operator:\s*(.*?)\n', block)
        crossover_match = re.search(r'CrossOver Operator:\s*(.*?)\s+Chance:\s*([\d\.]+)', block)
        mutation_match = re.search(r'Mutation Operator:\s*(.*?)\s+Chance:\s*([\d\.]+)', block)
        fitness_match = re.search(r'\[([\d,\s]+)\]', block)
        
        if fitness_match:
            survivor = survivor_match.group(1).strip() if survivor_match else "Unknown"
            mating = mating_match.group(1).strip() if mating_match else "Unknown"
            c_name = crossover_match.group(1).strip() if crossover_match else "Unk"
            c_chance = crossover_match.group(2) if crossover_match else "?"
            m_name = mutation_match.group(1).strip() if mutation_match else "Unk"
            m_chance = mutation_match.group(2) if mutation_match else "?"
            
            try:
                fitness_values = [int(x) for x in fitness_match.group(1).replace('\n', '').split(', ')]
            except ValueError:
                continue 

            final_score = fitness_values[-1]

            # Short label for hover
            label_text = (f"[Group {group_id}] Cost: {final_score}")
            
            # Store full config for this group if we haven't seen it yet
            if group_id not in group_configs:
                # Clean up the mating string to be shorter
                mating_short = mating.split('withReplacement')[0].strip()
                
                config_summary = (f"Mating: {mating_short} | "
                                  f"Cross: {c_name} ({c_chance}) | "
                                  f"Mut: {m_name} ({m_chance}) | "
                                  f"Surv: {survivor}")
                group_configs[group_id] = config_summary

            experiments.append({
                'group_id': group_id,
                'label': label_text, # Short label for hover
                'history': fitness_values,
                'final_score': final_score
            })

    return experiments, group_configs

def generate_dashboard(experiments, group_configs):
    if not experiments:
        print("No valid data found.")
        return

    plt.style.use('seaborn-v0_8-whitegrid')
    
    # Increase height slightly to accommodate the header
    fig, ax = plt.subplots(figsize=(16, 10)) 
    
    # Calculate how much space we need at the top based on number of groups
    # Base margin 0.85, subtract 0.03 for each group line
    unique_groups = sorted(list(group_configs.keys()))
    num_groups = len(unique_groups)
    
    top_margin = 0.85 - (num_groups * 0.02)
    plt.subplots_adjust(bottom=0.10, top=top_margin)

    # --- 1. Map Colors ---
    color_map = {gid: MANUAL_COLORS[i % len(MANUAL_COLORS)] for i, gid in enumerate(unique_groups)}

    # --- 2. Draw Header Info (Group Configs) ---
    # We print the text starting from the top
    start_y = 0.96
    fig.text(0.5, 0.98, "Experiment Configurations", ha='center', fontsize=14, fontweight='bold', color='#333333')
    
    for i, gid in enumerate(unique_groups):
        cfg_str = group_configs[gid]
        col = color_map[gid]
        
        # Format: "Group 0: [Configuration Details]"
        text_line = f"Group {gid}: {cfg_str}"
        
        # Place text on figure (coordinates are 0 to 1)
        fig.text(0.05, start_y - (i * 0.025), text_line, 
                 color=col, fontsize=10, fontweight='bold', fontfamily='monospace')

    # --- 3. Calculate Stats ---
    group_scores = {gid: [] for gid in unique_groups}
    for exp in experiments:
        group_scores[exp['group_id']].append(exp['final_score'])
    
    group_averages = {gid: sum(scores)/len(scores) for gid, scores in group_scores.items()}

    lines = []
    
    # --- 4. Draw Lines ---
    for exp in experiments:
        gid = exp['group_id']
        line_color = color_map.get(gid, 'black')
        
        # Combine generic label with specific fitness for hover
        full_hover_label = f"{exp['label']} (Avg for Group: {group_averages[gid]:.1f})"
        
        line, = ax.plot(
            exp['history'], 
            alpha=0.6,
            linewidth=1.5,
            color=line_color,
            picker=True,
            pickradius=5,
            label=full_hover_label
        )
        lines.append(line)

    # --- 5. Legend ---
    legend_elements = []
    for gid in unique_groups:
        avg = group_averages[gid]
        col = color_map[gid]
        label_text = f'Group {gid} (Avg Best: {avg:.1f})'
        legend_elements.append(Line2D([0], [0], color=col, lw=4, label=label_text))
    
    ax.legend(handles=legend_elements, loc='upper right', title="Performance Stats")

    # --- 6. Axes and Titles ---
    ax.set_xlabel("Iterations", fontsize=12, fontweight='bold')
    ax.set_ylabel("Fitness Cost", fontsize=12, fontweight='bold')
    
    # Subtitle inside the plot area
    title_obj = ax.set_title(
        f"Visualizing {len(experiments)} Runs",
        fontsize=12, color='#555555'
    )

    # --- 7. Interaction ---
    def on_move(event):
        if event.inaxes != ax: return

        highlighted = False
        for line in lines:
            if not line.get_visible(): continue 
            contains, _ = line.contains(event)
            if contains:
                line.set_alpha(1.0); line.set_linewidth(3.0); line.set_zorder(10)
                title_obj.set_text(line.get_label())
                title_obj.set_color(line.get_color()) 
                highlighted = True
            else:
                line.set_alpha(0.05); line.set_linewidth(1); line.set_zorder(1)
        
        if not highlighted:
            title_obj.set_text(f"Visualizing {sum(l.get_visible() for l in lines)} Runs")
            title_obj.set_color('#555555')
            for line in lines:
                if line.get_visible(): line.set_alpha(0.6)
        fig.canvas.draw_idle()

    # --- Filter Box ---
    axbox = plt.axes([0.15, 0.02, 0.7, 0.03]) # Moved further down
    text_box = TextBox(axbox, 'Filter: ', initial="", color='#f0f0f0', hovercolor='#ffffff')

    def submit(text):
        query = text.lower()
        count = 0
        for line in lines:
            if query in line.get_label().lower():
                line.set_visible(True); line.set_alpha(0.6); count += 1
            else:
                line.set_visible(False)
        if count > 0: ax.relim(); ax.autoscale_view()
        title_obj.set_text(f"Filter: '{text}' - {count} results")
        fig.canvas.draw_idle()

    text_box.on_submit(submit)
    fig.canvas.mpl_connect('motion_notify_event', on_move)

    print("Dashboard generated with Full Config Header.")
    plt.show()

if __name__ == "__main__":
    script_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(script_dir, TARGET_FILE)
    
    print(f"Reading: {file_path}")
    data, configs = read_and_parse_file(file_path)
    generate_dashboard(data, configs)