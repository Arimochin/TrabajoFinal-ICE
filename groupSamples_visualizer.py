import re 
import os 
import matplotlib as subplots
import matplotlib.pyplot as plt 
from matplotlib.widgets import TextBox 
from matplotlib.lines import Line2D 

TARGET_FILE = "output.txt" 
MANUAL_COLORS = ['red', 'blue', 'green', 'black', 'purple', 'orange', 'brown'] 

def read_and_parse_file(filepath): 
    """
    Parses configuration, fitness arrays, and statistical summary blocks.
    """
    experiments = [] 
    group_configs = {} 
    group_stats = {}
    
    if not os.path.exists(filepath): 
        return [], {}, {}

    with open(filepath, 'r', encoding='utf-8') as f: 
        content = f.read() 

    blocks = content.split('-----------------------------------------------------------------------------') 

    group_counter = 0 
    current_group_id = None

    for block in blocks: 
        block = block.strip()
        if not block: continue 
        
        if block.startswith("Group:"):
            avg_match = re.search(r'Average fitness:\s*([\d\.]+)', block)
            if avg_match and current_group_id is not None:
                group_stats[current_group_id] = float(avg_match.group(1))
            continue
        
        survivor_match = re.search(r'Survivor Selection:\s*(.*?)\n', block) 
        mating_match = re.search(r'Mating Pool Operator:\s*(.*?)\n', block) 
        crossover_match = re.search(r'CrossOver Operator:\s*(.*?)\s+Chance:\s*([\d\.]+)', block) 
        mutation_match = re.search(r'Mutation Operator:\s*(.*?)\s+Chance:\s*([\d\.]+)', block) 
        
        fitness_list_match = re.search(r'\[([\d,\s\n]+)\]', block) 
        
        if fitness_list_match: 
            try: 
                raw_values = fitness_list_match.group(1).replace('\n', ' ') 
                fitness_values = [int(x) for x in re.findall(r'\d+', raw_values)] 
            except ValueError: 
                continue 

            if not fitness_values: continue 

            survivor = survivor_match.group(1).strip() if survivor_match else "Unknown" 
            mating = mating_match.group(1).strip() if mating_match else "Unknown" 
            c_name = crossover_match.group(1).strip() if crossover_match else "Unk" 
            c_chance = crossover_match.group(2) if crossover_match else "?" 
            m_name = mutation_match.group(1).strip() if mutation_match else "Unk" 
            m_chance = mutation_match.group(2) if mutation_match else "?" 

            mating_short = mating.split('withReplacement')[0].strip() 
            
            config_summary = (f"Mating: {mating_short} | " 
                              f"Cross: {c_name} ({c_chance}) | " 
                              f"Mut: {m_name} ({m_chance}) | " 
                              f"Surv: {survivor}") 

            existing_id = next((gid for gid, cfg in group_configs.items() if cfg == config_summary), None) 
            
            if existing_id is None: 
                group_id = group_counter 
                group_configs[group_id] = config_summary 
                group_counter += 1 
            else: 
                group_id = existing_id 

            current_group_id = group_id

            final_score = fitness_values[-1] 
            label_text = f"[Group {group_id}] Cost: {final_score}" 

            experiments.append({ 
                'group_id': group_id, 
                'label': label_text, 
                'history': fitness_values, 
                'final_score': final_score 
            }) 

    return experiments, group_configs, group_stats

def generate_dashboard(experiments, group_configs, group_stats): 
    if not experiments: 
        return 

    plt.style.use('seaborn-v0_8-whitegrid') 
    fig, ax = plt.subplots(figsize=(16, 10))  
    
    unique_groups = sorted(list(group_configs.keys())) 
    num_groups = len(unique_groups) 
    
    top_margin = 0.90 - (num_groups * 0.025) 
    plt.subplots_adjust(bottom=0.12, top=max(0.7, top_margin)) 

    color_map = {gid: MANUAL_COLORS[i % len(MANUAL_COLORS)] for i, gid in enumerate(unique_groups)} 

    start_y = 0.96 
    fig.text(0.5, 0.98, "Experiment Configurations", ha='center', fontsize=14, fontweight='bold') 
    
    for i, gid in enumerate(unique_groups): 
        cfg_str = group_configs[gid] 
        col = color_map[gid] 
        text_line = f"Group {gid}: {cfg_str}" 
        fig.text(0.05, start_y - (i * 0.025), text_line,  
                 color=col, fontsize=9, fontweight='bold', fontfamily='monospace') 

    group_averages = {gid: group_stats.get(gid, 0.0) for gid in unique_groups}

    lines = [] 
    
    for exp in experiments: 
        gid = exp['group_id'] 
        line, = ax.plot( 
            exp['history'],  
            alpha=0.5, 
            linewidth=1.2, 
            color=color_map.get(gid, 'black'), 
            picker=True, 
            pickradius=5, 
            label=f"{exp['label']} (Avg: {group_averages[gid]:.1f})" 
        ) 
        lines.append(line) 

    legend_elements = [Line2D([0], [0], color=color_map[gid], lw=4,  
                       label=f'Group {gid} (Avg: {group_averages[gid]:.1f})') for gid in unique_groups] 
    ax.legend(handles=legend_elements, loc='upper right', title="Performance Stats") 

    ax.set_xlabel("Iterations", fontsize=12, fontweight='bold') 
    ax.set_ylabel("Fitness Cost", fontsize=12, fontweight='bold') 
    title_obj = ax.set_title(f"Visualizing {len(experiments)} Runs", fontsize=12, color='#555555') 

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
                if line.get_visible(): line.set_alpha(0.5) 
        fig.canvas.draw_idle() 

    axbox = plt.axes([0.2, 0.03, 0.6, 0.03]) 
    text_box = TextBox(axbox, 'Filter Group/Cost: ', initial="") 

    def submit(text): 
        query = text.lower() 
        count = 0 
        for line in lines: 
            if query in line.get_label().lower(): 
                line.set_visible(True); line.set_alpha(0.5); count += 1 
            else: 
                line.set_visible(False) 
        ax.relim(); ax.autoscale_view() 
        title_obj.set_text(f"Filter: '{text}' - {count} results") 
        fig.canvas.draw_idle() 

    text_box.on_submit(submit) 
    fig.canvas.mpl_connect('motion_notify_event', on_move) 
    plt.show() 

if __name__ == "__main__": 
    script_dir = os.path.dirname(os.path.abspath(__file__)) 
    file_path = os.path.join(script_dir, TARGET_FILE) 
    data, configs, stats = read_and_parse_file(file_path) 
    generate_dashboard(data, configs, stats)