import re
import os
import matplotlib.pyplot as plt
from matplotlib.widgets import TextBox

# ==========================================
# CONFIGURATION
# ==========================================
TARGET_FILE = "output-7.txt"
# ==========================================

def read_and_parse_file(filepath):
    """Reads the log file and extracts rich GA performance data."""
    experiments = []
    
    if not os.path.exists(filepath):
        print(f"\n[ERROR] File not found: {filepath}")
        return []

    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    blocks = content.split('-----------------------------------------------------------------------------')
    print(f"Found {len(blocks)} log blocks. Processing...")

    for block in blocks:
        if not block.strip(): continue
        
        # --- 1. Robust Regex Extraction ---
        # We capture everything to ensure no detail is lost
        survivor_match = re.search(r'Survivor Selection:\s*(.*?)\n', block)
        mating_match = re.search(r'Mating Pool Operator:\s*(.*?)\n', block)
        
        # Capture name AND chance for operators
        crossover_match = re.search(r'CrossOver Operator:\s*(.*?)\s+Chance:\s*([\d\.]+)', block)
        mutation_match = re.search(r'Mutation Operator:\s*(.*?)\s+Chance:\s*([\d\.]+)', block)
        
        fitness_match = re.search(r'\[([\d,\s]+)\]', block)
        
        if fitness_match and survivor_match:
            # Clean up strings
            survivor = survivor_match.group(1).strip()
            mating = mating_match.group(1).strip() if mating_match else "Unknown Mating"
            
            c_name = crossover_match.group(1).strip() if crossover_match else "Unk"
            c_chance = crossover_match.group(2) if crossover_match else "?"
            
            m_name = mutation_match.group(1).strip() if mutation_match else "Unk"
            m_chance = mutation_match.group(2) if mutation_match else "?"
            
            # --- 2. Construct the "Master Label" ---
            # This string appears in the header and is used for searching
            # Format: [Steady State] Mating: Tournament k:5 | PMX (0.8) | Inversion (0.1) | Fit: 5632
            
            fitness_values = [int(x) for x in fitness_match.group(1).split(', ')]
            final_score = fitness_values[-1]

            label = (f"[{survivor}] {mating} | "
                     f"{c_name} ({c_chance}) | "
                     f"{m_name} ({m_chance}) | "
                     f"Final Cost: {final_score}")
            
            experiments.append({
                'label': label,
                'history': fitness_values,
                'final_score': final_score
            })

    return experiments

def generate_dashboard(experiments):
    if not experiments:
        print("No valid data found.")
        return

    # Use a modern style
    plt.style.use('seaborn-v0_8-darkgrid')
    
    # Create the main figure and adjust layout to make room for the textbox at the bottom
    fig, ax = plt.subplots(figsize=(15, 9))
    plt.subplots_adjust(bottom=0.15) # Leave space at the bottom

    lines = []
    
    # --- 1. Draw Lines ---
    for exp in experiments:
        line, = ax.plot(
            exp['history'], 
            alpha=0.15,       # Start very faint
            linewidth=1.5,
            picker=True,      # Enable mouse detection
            pickradius=5,
            label=exp['label'] # This is the text we will search against
        )
        lines.append(line)

    # --- 2. Setup Axes ---
    ax.set_xlabel("Generations / Iterations", fontsize=11)
    ax.set_ylabel("Fitness Cost (Lower is Better)", fontsize=11)
    
    # The Dynamic Title
    header_text = ax.set_title(
        "HOVER to inspect | TYPE below to filter", 
        fontsize=12, fontweight='bold', color='#444444', 
        bbox=dict(facecolor='white', alpha=0.8, edgecolor='#cccccc', pad=5)
    )

    # --- 3. Interaction: Hover ---
    def on_move(event):
        if event.inaxes != ax: return

        found_highlight = False
        
        for line in lines:
            if not line.get_visible(): continue # Skip hidden filtered lines

            contains, _ = line.contains(event)
            if contains:
                # Highlight logic
                line.set_alpha(1.0)
                line.set_linewidth(3.0)
                line.set_zorder(10)
                
                # Update Header with full details
                header_text.set_text(line.get_label())
                header_text.set_color(line.get_color())
                found_highlight = True
            else:
                # Dim logic
                line.set_alpha(0.15)
                line.set_linewidth(1.5)
                line.set_zorder(1)
        
        if not found_highlight:
            header_text.set_text(f"Viewing {sum(l.get_visible() for l in lines)} Solutions (Hover line for details)")
            header_text.set_color('#444444')
            # Restore visibility for all filtered lines
            for line in lines:
                if line.get_visible():
                    line.set_alpha(0.5)

        fig.canvas.draw_idle()

    # --- 4. Interaction: Filter Box ---
    # Create an axis area for the text box [left, bottom, width, height]
    axbox = plt.axes([0.15, 0.05, 0.7, 0.05])
    text_box = TextBox(axbox, 'Filter Solutions: ', initial="", color='white', hovercolor='#f0f0f0')

    def submit(text):
        """Called when user hits Enter in the text box"""
        query = text.lower()
        count = 0
        
        for line in lines:
            label = line.get_label().lower()
            if query in label:
                line.set_visible(True)
                line.set_alpha(0.5)
                count += 1
            else:
                line.set_visible(False)
        
        # Reset view to fit the remaining visible lines
        if count > 0:
            ax.relim()
            ax.autoscale_view()
            
        header_text.set_text(f"Filter Applied: '{text}' - Found {count} solutions")
        fig.canvas.draw_idle()

    # Wire up events
    text_box.on_submit(submit)
    fig.canvas.mpl_connect('motion_notify_event', on_move)

    print("Dashboard generated. Filter box is at the bottom.")
    plt.show()

if __name__ == "__main__":
    script_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(script_dir, TARGET_FILE)
    
    print(f"Reading: {file_path}")
    data = read_and_parse_file(file_path)
    generate_dashboard(data)