import pandas as pd
import matplotlib.pyplot as plt

def parse_tsp_output(file_path):
    groups_data = []
    current_config_lines = []
    
    avg_fitness = None
    std_dev = None
    avg_time = None
    config_str = ""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        
    i = 0
    while i < len(lines):
        line = lines[i].strip()
        
        # Captura la configuracion al inicio de cada corrida
        if line.startswith("Initial Population:"):
            current_config_lines = [line]
            i += 1
            while i < len(lines) and not lines[i].startswith("["):
                if "Average fitness:" in lines[i] or "Best Composition:" in lines[i]:
                    break
                current_config_lines.append(lines[i].strip())
                i += 1
            config_str = "\n".join(current_config_lines)
            continue
            
        # Captura las metricas al final del grupo
        if line.startswith("Average fitness:"):
            avg_fitness = float(line.split(":")[1].strip())
        elif line.startswith("Standard Deviation:"):
            std_dev = float(line.split(":")[1].strip())
        elif line.startswith("Average execution time(Ms):"):
            avg_time = float(line.split(":")[1].strip().split()[0])
            
            # Se asume que el tiempo de ejecucion es la ultima metrica impresa en el resumen
            groups_data.append({
                'Configuración': config_str,
                'Fitness Promedio': avg_fitness,
                'Desviación Estándar': std_dev,
                'Tiempo Promedio (ms)': avg_time
            })
            
        i += 1
        
    df = pd.DataFrame(groups_data)
    
    # Asignacion de identificadores cortos para facilitar la visualizacion (iniciando en 0)
    df['ID'] = ['Group ' + str(idx) for idx in range(len(df))]
    return df

def plot_rankings(df):
    # Generacion de graficos independientes
    fig, axes = plt.subplots(1, 3, figsize=(18, 6))
    
    # 1. Ranking de Fitness Promedio (orden original)
    axes[0].bar(df['ID'], df['Fitness Promedio'], color='skyblue')
    axes[0].set_title('Ranking: Fitness Promedio')
    axes[0].set_ylabel('Fitness Promedio')
    
    # 2. Ranking de Desviacion Estandar (orden original)
    axes[1].bar(df['ID'], df['Desviación Estándar'], color='lightgreen')
    axes[1].set_title('Ranking: Desviación Estándar')
    axes[1].set_ylabel('Desviación Estándar')
    
    # 3. Ranking de Tiempo Promedio (orden original)
    axes[2].bar(df['ID'], df['Tiempo Promedio (ms)'], color='salmon')
    axes[2].set_title('Ranking: Tiempo Promedio (ms)')
    axes[2].set_ylabel('Tiempo (ms)')
    
    plt.tight_layout()
    plt.show()
    
    # Impresion de los detalles correspondientes a cada identificador
    print("Detalles de las Configuraciones:")
    for _, row in df.iterrows():
        print(f"\n--- {row['ID']} ---")
        print(row['Configuración'])

if __name__ == "__main__":
    df_results = parse_tsp_output('output.txt')
    print(df_results)
    plot_rankings(df_results)