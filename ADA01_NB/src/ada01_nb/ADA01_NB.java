package ada01_nb;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


public class ADA01_NB {
    
    public static void main(String[] args) {
        System.out.println("Graphs");
        
        int n = 100;
        int m = (int) ((n*n)*0.2);
        int d = (int) (n*0.2);
        
        //in in dirigido auto-ciclos
        
        //Modelo G(n,m) de Erdös y Rényi
        erdos_renyi(n, m, false, false);
        
        //Modelo G(n,p) de Gilbert
        //gilbert(n, 0.2, true, false);
        
        //Modelo G(n,r) geográfico simple:
        //geo_simple(n, 0.3, true, false);
        
        //Variante del modelo G(n,d) Barabási-Albert
        //barabasi_albert(n, d, true, false);
    }
    
    /*
    Variante del modelo G(n,d) Barabási-Albert
    1. colocar n vértices uno por uno, 
    asignando a cada uno d aristas a vértices distintos 
    de tal manera que la probabilidad de que el vértice nuevo se conecte a un vértice existente v 
    es proporcional a la cantidad de aristas que v tiene actualmente 
        *los primeros d vértices se conecta todos a todos
    */
    
    public static void barabasi_albert(int n, int d , boolean dirigido, boolean auto){
        //Repositorio de nodos
        HashMap hashMap_n = new HashMap();
        
        //Repositorio de aristas
        HashMap hashMap_m = new HashMap();
        
        //Inicialización de aristas
        int aristas = 1;
                     
        //Generar n nodos empezando por el 1
        for(int v = 1; v <= n; v++){
            String value = Integer.toString(v);            
            boolean respuesta = crear_nodo(v, value, hashMap_n);
            
            int nodo_origen = v;
            System.out.println("Se eligió el nodo origen: " + nodo_origen);
            
            //Checar si existe el nodo origen
            if(hashMap_n.containsKey(nodo_origen)){
                System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
            }
        
            if(hashMap_n.size() > d){
                
                boolean condicion = false;
                int aristas_por_nodo = 0;

                while(condicion != true){
                    //Elegir un nodo destino al azar
                    int nodo_destino = volado(1,v);
                    System.out.println("Se eligió al azar el nodo destino: " + nodo_destino);

                    //Checar si existe el nodo destino
                    if(hashMap_n.containsKey(nodo_destino)){
                        System.out.println("SÍ existe el nodo (key) " + nodo_destino); 
                    }
                    
                    //Se intenta crear el arista
                    System.out.println("Se intenta crear el arista " + nodo_origen + " + " + nodo_destino);
                    boolean respuesta_arista = crear_arista(nodo_origen, nodo_destino, aristas, hashMap_m, dirigido, auto);
                    
                    //Si se creó el arista, continua 
                    if(respuesta_arista == true){
                        aristas++;
                        aristas_por_nodo++;
                    }  
                    
                    double c_condicion = ((double) aristas_por_nodo) / ( (double) d);
                    
                    if(c_condicion == 1){
                        condicion = true;
                    }
                    System.out.println("CONDICION => " + "(" + aristas_por_nodo + "/" + d + ") = condicion " + c_condicion);
                }                
            }
        
        }
        //Se recorre el repositorio de aristas
        for(int i = 1; i <= hashMap_m.size(); i++){
            System.out.println("Arista " + i + ": " + hashMap_m.get(i));
        }
        
        //Se leen el tamaño de los repositorios de nodos y aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());
        
        System.out.println("*Generando archivo gexf");
        String file = "./resultados/grafos gexf/barabasi_albert" + "_" + n + "_" + d + ".gexf";
        //generar_gexf(hashMap_n, hashMap_m, dirigido, file);
                
        //String file_bfs = "./resultados/grafos gexf/generation/barabasi_albert_bfs" + "_" + n + "_" + d + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
    }
    
    /*
    Modelo G(n,r) geográfico simple:
    1. colocar n vértices en un rectángulo unitario con coordenadas uniformes (o normales) (x,y)
    2. colocar una arista entre cada par que queda en distancia r o menor
    */
    
    public static void geo_simple(int n, double r, boolean dirigido, boolean auto){
        //Repositorio de nodos
        HashMap hashMap_n = new HashMap();
        
        //Repositorio de aristas
        HashMap hashMap_m = new HashMap();
                     
        //Generar n nodos empezando por el 1
        for(int v = 1; v <= n; v++){
            double x = Math.random();
            double y = Math.random();
            String value = Double.toString(x) + ',' + Double.toString(y);
            
            boolean respuesta = crear_nodo(v, value, hashMap_n);
        }
        
        int aristas = 1;
        
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Elegir un nodo origen
            String nodo_origen = hashMap_n.get(i).toString();
            System.out.println("Se eligió el nodo origen: " + nodo_origen);
            
            //Checar si existe el nodo origen
            if(hashMap_n.containsKey(nodo_origen)){
                System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
            }
            
            //Checa los demás nodos menos ese
            for(int j = 1; j <= hashMap_n.size(); j++){
                String nodo_destino = hashMap_n.get(j).toString();
                System.out.println("Se eligió el nodo destino: " + nodo_destino);
                
                //Checar si existe el nodo destino
                if(hashMap_n.containsKey(nodo_destino)){
                    System.out.println("SÍ existe el nodo (key) " + nodo_destino); 
                }
                
                String[] parts_nodo_origen = nodo_origen.split(",");
                double x_nodo_origen = Double.parseDouble(parts_nodo_origen[0]);
                double y_nodo_origen = Double.parseDouble(parts_nodo_origen[1]);
                
                //System.out.println("NODO ORIGEN => " + "x: " + x_nodo_origen + " y : " + y_nodo_origen);
                
                String[] parts_nodo_destino = nodo_destino.split(",");
                double x_nodo_destino = Double.parseDouble(parts_nodo_destino[0]);
                double y_nodo_destino = Double.parseDouble(parts_nodo_destino[1]);
                
                //System.out.println("NODO DESTINO =>" + " x: " + x_nodo_destino + " y : " + y_nodo_destino);
                double distancia = Math.sqrt(Math.pow((x_nodo_origen - x_nodo_destino), 2) + Math.pow((y_nodo_origen - y_nodo_destino), 2));
                
                System.out.println("DISTANCIA => " + distancia);

                if(distancia <= r){
                    System.out.println("Se intenta crear el arista " + i + " + " + j);
                    boolean respuesta = crear_arista(i, j, aristas, hashMap_m, dirigido, auto);

                    //Si se creó el arista, se cuenta
                    if(respuesta == true){
                        aristas++;
                    }
                }
            }
        }
        
        //Se leen el tamaño de los repositorios de nodos y aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());
        
        for(int k = 1; k <= hashMap_m.size(); k++){
            System.out.println("Arista " + k + ": " + hashMap_m.get(k));
        }
        
        System.out.println("*Generando archivo gexf");
        String file = "./resultados/grafos gexf/geo_simple" + "_" + n + "_" + r + ".gexf";
        //generar_gexf(hashMap_n, hashMap_m, dirigido, file);
                
        String file_bfs = "./resultados/grafos gexf/geo_simple_bfs" + "_" + n + "_" + r + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
        
        //String file_dfs = "./resultados/grafos gexf/geo_simple_dfs_r" + "_" + n + "_" + r + ".gexf";
        
        //dfs_i(file_dfs, hashMap_n, hashMap_m, dirigido);
        
        String file_dfs = "./resultados/grafos gexf/geo_simple_dfs_r" + "_" + n + "_" + r + ".gexf";
        
        dfs_r(file_dfs, hashMap_n, hashMap_m, dirigido);
    } 
    
    /*
    Modelo G(n, p) de Gilbert:
    1. crear n vértices 
    2. crear una arista m entre cada par de nodos independiente y uniformemente con probabilidad p
    */
    
    public static void gilbert(int n, double p, boolean dirigido, boolean auto){
        //Repositorio de nodos
        HashMap hashMap_n = new HashMap();
        
        //Repositorio de aristas
        HashMap hashMap_m = new HashMap();
                     
        //Generar n nodos empezando por el 1
        for(int v = 1; v <= n; v++){
            String value = Integer.toString(v);
            boolean respuesta = crear_nodo(v, value, hashMap_n);
        }
        
        //Inicialización de aristas
        int aristas_totales = 0, aristas = 1;
        double t_n = (double) n;
        double t_p = (double) p;
        double exp = (double) 2;
       
        //valor esperado p*n^2 
        int valor_esperado = (int) (t_p*(Math.pow(t_n, exp)));
        
        System.out.println("valor esperado: " + valor_esperado);
        
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Elegir un nodo origen
            int nodo_origen = Integer.parseInt(hashMap_n.get(i).toString());
            System.out.println("Se eligió nodo destino: " + nodo_origen);
            
            //Checar si existe el nodo origen
            if(hashMap_n.containsKey(nodo_origen)){
                System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
            }
            
            int aristas_por_nodo = 0;
            while(aristas_por_nodo != (valor_esperado/n)){
                //Elegir un nodo destino al azar
                int nodo_destino = volado(1, n);

                System.out.println("Se eligió al azar el nodo destino: " + nodo_destino);

                //Checar si existe el nodo destino
                if(hashMap_n.containsKey(nodo_destino)){
                    System.out.println("SÍ existe el nodo (key) " + nodo_destino); 
                }
                
                System.out.println("Se intenta crear el arista " + nodo_origen + " + " + nodo_destino);
                boolean respuesta = crear_arista(nodo_origen, nodo_destino, aristas, hashMap_m, dirigido, auto);

                //Si se creó el arista, continua 
                    if(respuesta){
                        aristas++;
                        //aristas_totales++;
                        aristas_por_nodo++;
                    }
                }
                
                //System.out.println("ARISTAS DEL NODO " + i + ": "+ aristas_por_nodo);
            }
        
        //Se recorre el repositorio de aristas
        for(int i = 1; i <= hashMap_m.size(); i++){
            System.out.println("Arista " + i + ": " + hashMap_m.get(i));
        }
        
        //Se leen el tamaño de los repositorios de nodos y aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());
                        
        System.out.println("*Generando archivo gexf");
        String file = "./resultados/grafos gexf/gilbert" + "_" + n + "_" + p + ".gexf";
        //generar_gexf(hashMap_n, hashMap_m, dirigido, file);
        
        //String file_bfs = "./resultados/grafos gexf/gilbert_bfs" + "_" + n + "_" + p + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
        
        //String file_dfs = "./resultados/grafos gexf/gilbert_dfs_i" + "_" + n + "_" + p + ".gexf";
        
        //dfs_i(file_dfs, hashMap_n, hashMap_m, dirigido);
        
        String file_dfs = "./resultados/grafos gexf/gilbert_dfs_r" + "_" + n + "_" + p + ".gexf";
        
        dfs_r(file_dfs, hashMap_n, hashMap_m, dirigido);
    }
 
    /*
    Modelo G(n,m) de Erdös y Rényi:
    1. crear n vértices
    2. elegir uniformemente al azar m distintos pares de distintos vértices
    */

    public static void erdos_renyi(int n, int m, boolean dirigido, boolean auto){
        //Repositorio de nodos
        HashMap hashMap_n = new HashMap();
        
        //Repositorio de aristas
        HashMap hashMap_m = new HashMap();
        
        //Repositorio de pesos
        HashMap hashMap_w = new HashMap();
                     
        //Generar n nodos empezando por el 1
        for(int v = 1; v <= n; v++){
            String value = Integer.toString(v);
            boolean respuesta = crear_nodo(v, value, hashMap_n);
        }
        
        //Inicialización de aristas
        int aristas_totales = 0, aristas = 1;
        
        //Generar m aristas
        while(aristas_totales != m){
            //Elegir un nodo origen al azar
            int nodo_origen = volado(1, n);
            
            System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
            
            //Checar si existe el nodo origen
            if(hashMap_n.containsKey(nodo_origen)){
                System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
            }

            //Elegir un nodo destino al azar
            int nodo_destino = volado(1, n);
            
            System.out.println("Se eligió al azar el nodo destino: " + nodo_destino);
            
            //Checar si existe el nodo destino
            if(hashMap_n.containsKey(nodo_destino)){
                System.out.println("SÍ existe el nodo (key) " + nodo_destino); 
            }
            
            
            //Se intenta crear el arista
            System.out.println("Se intenta crear el arista " + nodo_origen + " + " + nodo_destino);
            boolean respuesta = crear_arista(nodo_origen, nodo_destino, aristas, hashMap_m, dirigido, auto);
            
            //Si se creó el arista, continua 
            if(respuesta){
                aristas++;
                aristas_totales++;                                
            }
        }

        //Se recorre el repositorio de aristas y se agrega el peso
        for(int i = 1; i <= hashMap_m.size(); i++){
            //System.out.println("Arista " + i + ": " + hashMap_m.get(i));
            //crearle un peso
            //int peso = volado(1, 10);
            //System.out.println("ID arista: " + hashMap_m.get(i) + " peso: " + peso);
            //boolean respuesta = crear_arista(nodo_origen, nodo_destino, aristas, hashMap_m, dirigido, auto);
            crear_peso(hashMap_m.get(i).toString(), hashMap_w);
        }
        
        //Se leen el tamaño de los repositorios de nodos y aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());
        
        System.out.println(" hashMap_n.keySet() " + hashMap_n.keySet()); 
        System.out.println(" hashMap_n.values() " + hashMap_n.values()); 
        
        System.out.println(" hashMap_m.keySet() " + hashMap_m.keySet()); 
        System.out.println(" hashMap_m.values() " + hashMap_m.values()); 
        
        System.out.println(" hashMap_w.keySet() " + hashMap_w.keySet()); 
        System.out.println(" hashMap_w.values() " + hashMap_w.values()); 
        
        System.out.println("*Generando archivo gexf");
        String file = "./resultados/grafos gexf/erdos_renyi" + "_" + n + "_" + m + ".gexf";
        
        //generar_gexf(hashMap_n, hashMap_m, hashMap_w, dirigido, file);
        
        String file_kruskal = "./resultados/grafos gexf/kruskal/erdos_renyi_kruskal" + "_" + n + "_" + m + ".gexf";
        //String file_kruskal = "./resultados/grafos gexf/erdos_renyi_kruskal" + "_" + n + "_" + m + ".gexf";
        kruskal(file_kruskal, hashMap_n, hashMap_m, hashMap_w, dirigido);
        //generar_gexf(hashMap_n, hashMap_m, hashMap_w, dirigido, file);
        
        //String file_bfs = "./resultados/grafos gexf/erdos_renyi_dfs_i" + "_" + n + "_" + m + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
        
        //String file_dfs = "./resultados/grafos gexf/erdos_renyi_dfs_r" + "_" + n + "_" + m + ".gexf";
        
        //dfs_r(file_dfs, hashMap_n, hashMap_m, dirigido);
    }
        
    public static void kruskal(String file, HashMap hashMap_n, HashMap hashMap_m, HashMap hashMap_w, boolean dirigido){
        
        //LEE TODOS LOS NODOS
        String conector = null;
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    Integer peso = Integer.parseInt(hashMap_w.get(candidato).toString());

                    if(nodo == key_origen){
                        System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino + " peso: " + peso);                    
                        contador++;
                    }
                }            
            }
        }
                                
        int temp = hashMap_n.size();
        
        HashMap matrix = new HashMap();
        
        int[][] ady = new int[temp][temp];
        
        System.out.println(temp);
        System.out.println(ady.length);
        
        /*
        for(int i = 0; i < temp; i++){
            for(int j = 0; j < temp; j++){
                ady[i][j] = 9999;
                //System.out.print(ady[i][j]);
                if(i==j){
                    ady[i][j] = 0;
                }
            }
            //System.out.println();
        }       
        */        
        
        //int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                //System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    Integer peso = Integer.parseInt(hashMap_w.get(candidato).toString());

                    if(nodo == key_origen){
                        //System.out.println(contador + " Nodo adyacente a " + nodo + conector + key_destino + " peso: " + peso);                    
                        //contador++;
                        int o = key_origen-1;
                        int p = key_destino-1;
                        ady[o][p] = (int) peso;
                        //System.out.println(" se agregó " + o + conector + p + " ady: " + ady[o][p]);  
                        
                        String h = o+conector+p;
                        matrix.put(h, peso);
                    }
                }            
            }
        }
        
        System.out.println(" matrix.size() " + matrix.size()); 
        System.out.println(" matrix.values()) " + matrix.values()); 
        System.out.println(" matrix.keySet() " + matrix.keySet()); 
        
        matrix = sortByValues(matrix);
        
        System.out.println(" matrix.size() " + matrix.size()); 
        System.out.println(" matrix.values()) " + matrix.values()); 
        System.out.println(" matrix.keySet() " + matrix.keySet()); 
        
        HashMap descubiertos = new HashMap(); //nodos
        HashMap no_descubiertos = new HashMap(); //nodos
        
        for(int i = 1; i <= hashMap_n.size(); i++){        
            no_descubiertos.put(i, i);            
        }
        
        HashMap arbol_kruskal = new HashMap(); //nodos
        HashMap arbol_kruskal_peso = new HashMap(); //nodos
        HashMap arbol_kruskal_peso_reves = new HashMap(); //nodos
        
        System.out.println(" descubiertos.values()) " + descubiertos.values()); 
        System.out.println(" no_descubiertos.values() " + no_descubiertos.values()); 
        
        //while(!no_descubiertos.isEmpty()){
        for ( Object key : matrix.keySet() ) {
            String candidato = key.toString();
            String[] parts = candidato.split(conector);
            Integer key_origen = Integer.parseInt(parts[0]);
            key_origen = key_origen + 1;

            Integer key_destino = Integer.parseInt(parts[1]);
            key_destino = key_destino + 1;

            Integer peso = Integer.parseInt(matrix.get(key).toString());            

            //System.out.println(peso + "\t" + (key_origen) + "\t" + (key_destino) + "\t");

            String candidato2 = key_origen + conector + key_destino;
            arbol_kruskal.put(peso, candidato2);


            if(!descubiertos.containsKey(key_origen)){ 
                descubiertos.put(key_origen, key_origen);
                arbol_kruskal.put(peso, candidato2);
                arbol_kruskal_peso.put(candidato2, peso);
            }
            //if(!descubiertos.containsKey(key_destino)){ descubiertos.put(key_destino, key_destino);}

        }
        //}
        
        contador = 1;
        for ( Object key2 : arbol_kruskal_peso.keySet() ) {
            String can = key2.toString();
            //int g = Integer.parseInt(arbol_kruskal_peso.get(key2).toString());            
            
            arbol_kruskal_peso_reves.put(contador, can);
            contador++;
        }
        
        /*System.out.println(" arbol_kruskal.size() " + arbol_kruskal.size()); 
        System.out.println(" arbol_kruskal.values()) " + arbol_kruskal.values()); 
        System.out.println(" arbol_kruskal.keySet() " + arbol_kruskal.keySet());
        System.out.println("------------------------------------------");*/
        System.out.println(" arbol_kruskal_peso.size() " + arbol_kruskal_peso.size()); 
        System.out.println(" arbol_kruskal_peso.values()) " + arbol_kruskal_peso.values()); 
        System.out.println(" arbol_kruskal_peso.keySet() " + arbol_kruskal_peso.keySet());
        System.out.println("------------------------------------------");
        System.out.println(" arbol_kruskal_peso_reves.size() " + arbol_kruskal_peso_reves.size()); 
        System.out.println(" arbol_kruskal_peso_reves.values()) " + arbol_kruskal_peso_reves.values()); 
        System.out.println(" arbol_kruskal_peso_reves.keySet() " + arbol_kruskal_peso_reves.keySet());
        /*System.out.println("------------------------------------------");
        System.out.println(" descubiertos.size() " + descubiertos.size()); 
        System.out.println(" descubiertos.values()) " + descubiertos.values()); 
        System.out.println(" descubiertos.keySet() " + descubiertos.keySet());
        System.out.println("------------------------------------------");*/
        System.out.println("*Generando archivo gexf");        
        
        
        generar_gexf(descubiertos, arbol_kruskal_peso_reves, arbol_kruskal_peso, dirigido, file);
        
        
        for(int i = 1; i <= 10; i++){
            //Si existe
            
            if(matrix.containsValue(i)){
                //System.out.println( (matrix.entrySet(matrix.containsValue(i))) );
                
               
                
                //System.out.println(matrix.get(i));
                 
                //int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                //System.out.println("nodo a evaluar: " + nodo);     
                
                //System.out.println("PESO: " + i);
                
                
                //for(int j = 1; j <= matrix.size(); j++){
                //}
                
                //for(int j = 1; j <= matrix.size(); j++){
                    
                    //String candidato = matrix.containsKey(ady);
                    /*String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);*/
                    //Integer peso = Integer.parseInt(matrix.get(candidato).toString());

                    //System.out.println("akjshaksaks " + key_origen + conector + key_destino + " peso: " + peso);
                    
                    /*
                    if(i == peso){
                        //System.out.println(contador + " akjshaksaks " + nodo + conector + key_destino + " peso: " + peso);
                        //System.out.println(contador + " Nodo adyacente a " + nodo + conector + key_destino + " peso: " + peso);                    
                        //contador++;
                        int o = key_origen-1;
                        int p = key_destino-1;
                        ady[o][p] = (int) peso;
                        System.out.println(" se agregó " + o + conector + p + " ady: " + ady[o][p]);  
                        
                        String h = o+conector+p;
                        matrix.put(h, peso);
                    }*/
                //}   
                
                
                
            }
        }
        
        
        /*
        System.out.println("ORDENAMIENTO");       
        HashMap matrix_ordenado = new HashMap();          
        contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(nodos_descubiertos.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 0; j < matrix.size(); j++){
                    if(arbol_bfs.containsKey(j)){
                        String candidato = arbol_bfs.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);

                        if(nodo == key_origen){
                            //System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);     
                            System.out.println(key_origen + conector+ key_destino);//contador++;
                            matrix_ordenado.put(contador, key_origen + conector+ key_destino);
                            contador++;
                        }
                    }                    
                }            
            }
        } */             
        
        //System.out.println(" arbol_bfs_ordenado.size() " + arbol_bfs_ordenado.size() + " " + arbol_bfs_ordenado.values()); 
        
        /*
        System.out.println("3");
        for(int i = 0; i < temp; i++){
            System.out.print(i+1 + " " );
        }
        System.out.println();
        for(int i = 0; i < temp; i++){
            System.out.print(i+1 + " ");
            for(int j = 0; j < temp; j++){
                System.out.print(ady[i][j] + " ");
            }
            System.out.println();
        }
        */
                       /*
        for(int i = 0; i < temp; i++){            
            for(int j = 0; j < temp; j++){
                int l = ady[i][j];
                if(ady[i][j] == 0 && ady[i][j] == 9999){
                    ady[j][i] = l;
                }
                
            }            
        }                
        
        System.out.println();
        for(int i = 0; i < temp; i++){
            System.out.print(i+1 + "\t");
            for(int j = 0; j < temp; j++){
                System.out.print(ady[i][j] + "\t");
            }
            System.out.println();
        }
        
        //Escoger un nodo al azar dentro de los nodos existentes
        int nodo_origen = volado(1, hashMap_n.size());            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
        
        int nodo_destino = volado(1, hashMap_n.size());            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_destino);
        
        */
        
    }
    
    public static boolean crear_nodo(int key, String value, HashMap hashMap_n){
        boolean respuesta = false;

        // checa el hashmap
        // Si ya existe el nodo, entonces NO lo agrega al repositorio
        //
        if(hashMap_n.containsValue(value)){
        //if(hashMap_n.containsKey(key)){
            System.out.println("NO se creó el nodo => (key) " + key + " (value) " + value);
            //Regresa falso
            respuesta = false;
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        //}else if(!hashMap_n.containsKey(key)){
        }else if(!hashMap_n.containsValue(value)){
            //Crea el nodo con key y value
            System.out.println("Se creó el nodo => (key) " + key + " (value) " + value);
            hashMap_n.put(key, value);
            
            //Regresa verdadero
            respuesta = true;
        }

        return respuesta;
    }

    public static boolean crear_peso(String key, HashMap hashMap_w){
        boolean respuesta;
        
        //Se agrega el peso al arista      
        hashMap_w.put(key, volado(1, 10));

        System.out.println("Se agregó el peso: " + hashMap_w.get(key) + " al artista: " + key);

        //Regresa verdadero
        respuesta = true;
        
        return respuesta;
    }    
    
    public static boolean crear_arista(int key_nodo_u, int key_nodo_v, int key, HashMap hashMap_m, boolean dirigido, boolean auto){
        boolean respuesta = false, validacion = false;
        String arista = null;
        
        //Si dirigido es verdadero, el grafo es dirigido
        if(dirigido == true){
            arista = key_nodo_u + "->" + key_nodo_v;
            
        //Si dirigido es falso, el grafo es no dirigido
        }else if(dirigido == false){
            arista = key_nodo_u + "--" + key_nodo_v;
        }
              
        System.out.println("Validando el arista: " + arista);
                
        //Si pueden existir autociclos, u y v pueden ser distintos o iguales
        if(auto == true){
            if(key_nodo_u == key_nodo_v || key_nodo_u != key_nodo_v){
                validacion = true;
            }
            
        //Si no pueden existir autociclos, u y v no pueden ser iguales
        }else if(auto == false ){
            if(key_nodo_u == key_nodo_v){
                System.out.println("No se puede crear el arista: " + arista);
                validacion = false;
            }else if(key_nodo_u != key_nodo_v){
                validacion = true;
            }
        }
        
        if(validacion == true){
            // checa el hashmap
            // Si existe el arista, NO lo agrega al repositorio
            if(hashMap_m.containsValue(arista)){
                System.out.println("Ya existe el arista: " + arista);

                //Regresa falso
                respuesta = false;

            // Si no existe el arista, entonces SÍ lo agrega al repositorio
            }else if(!hashMap_m.containsValue(arista)){
                //Crea el arista con key y value
                hashMap_m.put(key, arista);

                System.out.println("Se creó el arista: " + hashMap_m.get(key));

                //Regresa verdadero
                respuesta = true;
            }
        }
                           
       
        return respuesta;
    }    

    public static int volado(int min, int max){
        return (int) Math.floor(Math.random()*(max-min+1)+(min));
    }
    
    public static void generar_gexf(HashMap hashMap_n, HashMap hashMap_m, HashMap hashMap_w, boolean dirigido, String archivo){
    //public static void generar_gexf(HashMap hashMap_n, HashMap hashMap_m, boolean dirigido, String archivo){  
        String conector = null;
        
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        //Init a project - and therefore a workspace
        ProjectController pc_erdos_renyi = Lookup.getDefault().lookup(ProjectController.class);
        pc_erdos_renyi.newProject();
        Workspace ws_erdos_renyi = pc_erdos_renyi.getCurrentWorkspace();
        
        //Get a graph model - it exists because we have a workspace
        GraphModel gm_erdos_renyi = Lookup.getDefault().lookup(GraphController.class).getGraphModel(ws_erdos_renyi);
        
        //Append as a Directed Graph
        DirectedGraph directedGraph = gm_erdos_renyi.getDirectedGraph();
        
        //Se recorre el repositorio de nodos
        for(int i = 1; i <= hashMap_n.size(); i++){
            
            //System.out.println("GEXF n => " + "(key) " + hashMap_n.get(i));
            String nodo = Integer.toString(i);
            Node n = gm_erdos_renyi.factory().newNode(nodo);
            n.setLabel(hashMap_n.get(i).toString());
            n.setColor(Color.decode("#ff99dc"));
            
            System.out.println("GEXF n => " + "(key) " + i + " (value) " + n.getLabel());
                
            //Append
            directedGraph.addNode(n); 
        }
        
        //Se recorre el repositorio de aristas
        for(int i = 1; i <= hashMap_m.size(); i++){
            //System.out.println("GEXF m => Arista " + i + ": " + hashMap_m.get(i));
            String arista = hashMap_m.get(i).toString();            
            Integer peso = Integer.parseInt(hashMap_w.get(arista).toString());
            
            String[] parts = arista.split(conector);

            
            Integer key_origen = Integer.parseInt(parts[0]);
            Node nodo_origen = directedGraph.getNode(key_origen.toString());//gm_erdos_renyi.getGraph().getNode(key_origen);

            Integer key_destino = Integer.parseInt(parts[1]);
            Node nodo_destino = directedGraph.getNode(key_destino.toString());//gm_erdos_renyi.getGraph().getNode(key_destino);

            System.out.println("GEXF m => " + " (key) " + nodo_origen.getId().toString() + " (key) " + nodo_destino.getId().toString() + " (weight) " + peso);
            //System.out.println("GEXF m => " + " (key) " + nodo_origen.getId().toString() + " (key) " + nodo_destino.getId().toString());
            
            //Edge e = gm_erdos_renyi.factory().newEdge(nodo_origen, nodo_destino, dirigido); 
            Edge e = gm_erdos_renyi.factory().newEdge(nodo_origen, nodo_destino, 0, peso, dirigido); 
            //gm_erdos_renyi.factory().newEdge(nodo_origen, nodo_destino, peso, dirigido);
            //gm_erdos_renyi.factory().newEdge(nodo_origen, nodo_origen, 2, peso, dirigido); 
            e.setColor(Color.decode("#adb3ff"));

            //Apend
            directedGraph.addEdge(e);

        }
        
        
        //bfs(hashMap_n, hashMap_m, dirigido);
        
        
        //Export full graph
        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);
        
        
        try{
            exportController.exportFile(new File(archivo), ws_erdos_renyi);
            System.out.println("Se creó el archivo: " + archivo);
        }catch(IOException e){
            System.out.println(e);
        }   

        
    }
        
    private static HashMap sortByValues(HashMap map) { 
       List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       } 
       return sortedHashMap;
    }
    
    /*
    public static void dijkstra(String file, HashMap hashMap_n, HashMap hashMap_m, HashMap hashMap_w, boolean dirigido){
        HashMap hashMap_pq = new HashMap();
        HashMap hashMap_pq_w = new HashMap();
        
        HashMap nodos_descubiertos = new HashMap();
        HashMap nodos_no_descubiertos = new HashMap();
        HashMap nodos_descubiertos_w = new HashMap();
        
        HashMap aristas_dijkstra = new HashMap(); 
        
        //LEE TODOS LOS NODOS
        String conector = null;
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    double peso = Double.parseDouble(hashMap_w.get(candidato).toString());

                    if(nodo == key_origen){
                        System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino + " peso: " + peso);                    
                        contador++;
                    }
                }            
            }
        }
        
        //Escoger un nodo al azar dentro de los nodos existentes
        int nodo_origen = volado(1, hashMap_n.size());
            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
            
        //Checar si existe el nodo origen
        if(hashMap_n.containsKey(nodo_origen)){
            System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
        }
        
        int key = nodo_origen;
        String value = String.valueOf(nodo_origen);
                        
        //Intenta agregarlo
        if(nodos_descubiertos.containsValue(value)){
            System.out.println("NO se descubrió el nodo => (key) " + key + " (value) " + value);
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        //}else if(!hashMap_n.containsKey(key)){
        }else if(!nodos_descubiertos.containsValue(value)){
            //Crea el nodo con key y value
            System.out.println("Se descubrió el nodo => (key) " + key + " (value) " + value);
            nodos_descubiertos.put(key, key); 
            hashMap_pq.put(key, key); 
            hashMap_pq_w.put(key, 0.0); 
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //System.out.println("hashMap_n.get(i): " + hashMap_n.get(i));
                
                int nodo_existente = i;//Integer.parseInt(hashMap_n.get(i).toString());
                
                //System.out.println("nodo_existente: " + nodo_existente);
                if(nodo_existente != key){
                    nodos_no_descubiertos.put(nodo_existente, nodo_existente);
                }
            }
        }
        
        System.out.println("hashMap_pq: " + hashMap_pq.values());
        System.out.println("hashMap_pq_w: " + hashMap_pq_w.values());
        
                          
    }
    */
    
    public static void bfs(String file, HashMap hashMap_n, HashMap hashMap_m, boolean dirigido){          
        String conector = null;
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        //LEE TODOS LOS NODOS
        
        int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);

                    if(nodo == key_origen){
                        System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);                    
                        contador++;
                    }
                }            
            }
        }
        
        //Repositorio de nodos descubiertos
        HashMap nodos_descubiertos = new HashMap();
        HashMap nodos_no_descubiertos = new HashMap();
                
        //Escoger un nodo al azar dentro de los nodos existentes
        int nodo_origen = volado(1, hashMap_n.size());
            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
            
        //Checar si existe el nodo origen
        if(hashMap_n.containsKey(nodo_origen)){
            System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
        }
        
        int key = nodo_origen;
        String value = String.valueOf(nodo_origen);
                        
        //Intenta agregarlo
        if(nodos_descubiertos.containsValue(value)){
            System.out.println("NO se descubrió el nodo => (key) " + key + " (value) " + value);
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        //}else if(!hashMap_n.containsKey(key)){
        }else if(!nodos_descubiertos.containsValue(value)){
            //Crea el nodo con key y value
            System.out.println("Se descubrió el nodo => (key) " + key + " (value) " + value);
            nodos_descubiertos.put(key, value);   
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //System.out.println("hashMap_n.get(i): " + hashMap_n.get(i));
                
                int nodo_existente = i;//Integer.parseInt(hashMap_n.get(i).toString());
                
                //System.out.println("nodo_existente: " + nodo_existente);
                if(nodo_existente != key){
                    nodos_no_descubiertos.put(nodo_existente, nodo_existente);
                }
            }
        }    
        
        System.out.println("EXISTENCIA**************"); 
        //checar si tiene conexiones        
        for(int l =1; l <= hashMap_n.size();l++){
            if(hashMap_n.containsKey(l)){ 
                int nodo_evaluador = l;//Integer.parseInt(hashMap_n.get(l).toString());                      
                boolean existencia = false;
                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    
                    //Si está en algun lado
                    if(nodo_evaluador == key_destino){
                       existencia = true; 
                    }
                }
                
                if(existencia == false){
                    nodos_descubiertos.put(nodo_evaluador, nodo_evaluador);
                    nodos_no_descubiertos.remove(nodo_evaluador);
                    System.out.println("El nodo evaluador: " + nodo_evaluador + "es conectado por alguno?: " + existencia); 
                }
            }
        }
                       
        HashMap arbol_bfs = new HashMap();              
        
        int explorador = 1;     
        contador = 1;
        //for(int i = 1; i <= hashMap_n.size(); i++){
        while(!(nodos_no_descubiertos.isEmpty())){      
            //System.out.println("jjjjjjjj********");                        
            
            for(int i = 1; i <= hashMap_n.size(); i++){                                
                
            //System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
            System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
            System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
                
            System.out.println("nodos_descubiertos.containsKey: " + i + " "+ nodos_descubiertos.containsKey(i));
                if(nodos_descubiertos.containsKey(i)){                                                                             
                    int nodo_evaluador = Integer.parseInt(nodos_descubiertos.get(i).toString());                      
                      
                    //System.out.println("nodo a evaluar: " + nodo_evaluador);   
                                                            
                    for(int j = 1; j <= hashMap_m.size(); j++){
                        String candidato = hashMap_m.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);
                        
                        //System.out.println(contador + " " + key_origen + conector+ key_destino);contador++;

                        if(nodo_evaluador == key_origen){  
                            if(nodos_descubiertos.containsKey(key_origen) && nodos_no_descubiertos.containsKey(key_destino)){
                                System.out.println(contador + " " + key_origen + conector+ key_destino);contador++;
                                
                                arbol_bfs.put(contador, key_origen + conector+ key_destino);

                                nodos_no_descubiertos.remove(key_destino);
                                nodos_descubiertos.put(key_destino, key_destino);

                            }
                        }
                    }
                }
            }
        }
        
        System.out.println(" arbol_bfs.size() " + arbol_bfs.size() + " " + arbol_bfs.values());        
        
        System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
        System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
        
        System.out.println("ORDENAMIENTO");       
        HashMap arbol_bfs_ordenado = new HashMap();          
        contador = 1;
        for(int i = 1; i <= nodos_descubiertos.size(); i++){
            //Si existe
            if(nodos_descubiertos.containsKey(i)){
                int nodo = Integer.parseInt(nodos_descubiertos.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= arbol_bfs.size()+1; j++){
                    if(arbol_bfs.containsKey(j)){
                        String candidato = arbol_bfs.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);

                        if(nodo == key_origen){
                            //System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);     
                            System.out.println(key_origen + conector+ key_destino);//contador++;
                            arbol_bfs_ordenado.put(contador, key_origen + conector+ key_destino);
                            contador++;
                        }
                    }                    
                }            
            }
        }              
        
        System.out.println(" arbol_bfs_ordenado.size() " + arbol_bfs_ordenado.size() + " " + arbol_bfs_ordenado.values());        
        
        System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
        System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
        
        System.out.println("*Generando archivo gexf");        
        //generar_gexf(nodos_descubiertos, arbol_bfs_ordenado, dirigido, file);
        
    }
   
    public static void dfs_i(String file, HashMap hashMap_n, HashMap hashMap_m, boolean dirigido){
        String conector = null;
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        HashMap arbol_dfs = new HashMap();     
        
        //LEE TODOS LOS NODOS
        System.out.println("**********LECTURA**********");        
        int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);

                    if(nodo == key_origen){
                        System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);                    
                        contador++;
                    }
                }            
            }
        }
        
        //Repositorio de nodos descubiertos
        HashMap nodos_descubiertos = new HashMap();
        HashMap nodos_no_descubiertos = new HashMap();
                
        //Escoger un nodo al azar dentro de los nodos existentes
        int nodo_origen = volado(1, hashMap_n.size());
            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
            
        //Checar si existe el nodo origen
        if(hashMap_n.containsKey(nodo_origen)){
            System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
        }
        
        int key = nodo_origen;
        String value = String.valueOf(nodo_origen);
                        
        //Intenta agregarlo
        if(nodos_descubiertos.containsValue(value)){
            System.out.println("NO se descubrió el nodo => (key) " + key + " (value) " + value);
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        //}else if(!hashMap_n.containsKey(key)){
        }else if(!nodos_descubiertos.containsValue(value)){
            //Crea el nodo con key y value
            System.out.println("Se descubrió el nodo => (key) " + key + " (value) " + value);
            nodos_descubiertos.put(key, value);   
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //System.out.println("hashMap_n.get(i): " + hashMap_n.get(i));
                
                int nodo_existente = i;//Integer.parseInt(hashMap_n.get(i).toString());
                
                //System.out.println("nodo_existente: " + nodo_existente);
                if(nodo_existente != key){
                    nodos_no_descubiertos.put(nodo_existente, nodo_existente);
                }
            }
        }    
        
       
        System.out.println("EXISTENCIA**************"); 
        //checar si tiene conexiones        
        for(int l =1; l <= hashMap_n.size();l++){
            if(hashMap_n.containsKey(l)){ 
                int nodo_evaluador = l;//Integer.parseInt(hashMap_n.get(l).toString());                      
                boolean existencia = false;
                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    
                    //Si está en algun lado
                    if(nodo_evaluador == key_destino){
                       existencia = true; 
                    }
                }
                
                if(existencia == false){
                    nodos_descubiertos.put(nodo_evaluador, nodo_evaluador);
                    nodos_no_descubiertos.remove(nodo_evaluador);
                    System.out.println("El nodo evaluador: " + nodo_evaluador + "es conectado por alguno?: " + existencia); 
                }                
            }
        }              
                        
        System.out.println("DFS ITERATIVO**************");         
        contador = 1;
        while(!(nodos_no_descubiertos.isEmpty())){
            System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
            System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //Si existe
                if(hashMap_n.containsKey(i)){
                    int nodo_evaluador = i;//Integer.parseInt(hashMap_n.get(i).toString());
                    //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                    System.out.println("nodo a evaluar: " + nodo_evaluador);        

                    for(int j = 1; j <= hashMap_m.size(); j++){
                        String candidato = hashMap_m.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);

                        if(nodo_evaluador == key_origen){                        
                            if(nodos_descubiertos.containsKey(key_origen) && nodos_no_descubiertos.containsKey(key_destino)){
                                System.out.println(contador + " " + key_origen + conector+ key_destino);contador++;

                                arbol_dfs.put(contador, key_origen + conector+ key_destino);

                                nodos_no_descubiertos.remove(key_destino);
                                nodos_descubiertos.put(key_destino, key_destino);

                                System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
                                System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
                            }                    
                        }
                    }            
                }
            }
        }
                        
        System.out.println("ORDENAMIENTO");       
        HashMap arbol_dfs_ordenado = new HashMap();          
        contador = 1;
        for(int i = 1; i <= nodos_descubiertos.size(); i++){
            //Si existe
            if(nodos_descubiertos.containsKey(i)){
                int nodo = Integer.parseInt(nodos_descubiertos.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= arbol_dfs.size()+1; j++){
                    if(arbol_dfs.containsKey(j)){
                        String candidato = arbol_dfs.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);

                        if(nodo == key_origen){
                            //System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);     
                            System.out.println(key_origen + conector+ key_destino);//contador++;
                            arbol_dfs_ordenado.put(contador, key_origen + conector+ key_destino);
                            contador++;
                        }
                    }                    
                }            
            }
        }              
        
        System.out.println(" arbol_bfs_ordenado.size() " + arbol_dfs_ordenado.size() + " " + arbol_dfs_ordenado.values());        
        
        System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
        System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
        
        System.out.println("*Generando archivo gexf");        
        //generar_gexf(nodos_descubiertos, arbol_dfs_ordenado, dirigido, file);
    }
    
    public static void dfs_r(String file, HashMap hashMap_n, HashMap hashMap_m, boolean dirigido){
        String conector = null;
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        HashMap arbol_dfs = new HashMap();     
        
        //LEE TODOS LOS NODOS
        System.out.println("**********LECTURA**********");        
        int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);

                    if(nodo == key_origen){
                        System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);                    
                        contador++;
                    }
                }            
            }
        }
        
        //Repositorio de nodos descubiertos
        HashMap nodos_descubiertos = new HashMap();
        HashMap nodos_no_descubiertos = new HashMap();
                
        //Escoger un nodo al azar dentro de los nodos existentes
        int nodo_origen = volado(1, hashMap_n.size());
            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
            
        //Checar si existe el nodo origen
        if(hashMap_n.containsKey(nodo_origen)){
            System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
        }
        
        int key = nodo_origen;
        String value = String.valueOf(nodo_origen);
                        
        //Intenta agregarlo
        if(nodos_descubiertos.containsValue(value)){
            System.out.println("NO se descubrió el nodo => (key) " + key + " (value) " + value);
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        //}else if(!hashMap_n.containsKey(key)){
        }else if(!nodos_descubiertos.containsValue(value)){
            //Crea el nodo con key y value
            System.out.println("Se descubrió el nodo => (key) " + key + " (value) " + value);
            nodos_descubiertos.put(key, value);   
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //System.out.println("hashMap_n.get(i): " + hashMap_n.get(i));
                
                int nodo_existente = i;//Integer.parseInt(hashMap_n.get(i).toString());
                
                //System.out.println("nodo_existente: " + nodo_existente);
                if(nodo_existente != key){
                    nodos_no_descubiertos.put(nodo_existente, nodo_existente);
                }
            }
        }    
        
       
        System.out.println("EXISTENCIA**************"); 
        //checar si tiene conexiones        
        for(int l =1; l <= hashMap_n.size();l++){
            if(hashMap_n.containsKey(l)){ 
                int nodo_evaluador = l;//Integer.parseInt(hashMap_n.get(l).toString());                      
                boolean existencia = false;
                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    
                    //Si está en algun lado
                    if(nodo_evaluador == key_destino){
                       existencia = true; 
                    }
                }
                
                if(existencia == false){
                    nodos_descubiertos.put(nodo_evaluador, nodo_evaluador);
                    nodos_no_descubiertos.remove(nodo_evaluador);
                    System.out.println("El nodo evaluador: " + nodo_evaluador + "es conectado por alguno?: " + existencia); 
                }                
            }
        }              
                        
        System.out.println("DFS ITERATIVO**************");         
        contador = 1;
        recursivo(contador, conector, hashMap_n, hashMap_m, nodos_descubiertos, nodos_no_descubiertos, arbol_dfs);
        /*
        while(!(nodos_no_descubiertos.isEmpty())){
            System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
            System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //Si existe
                if(hashMap_n.containsKey(i)){
                    int nodo_evaluador = i;//Integer.parseInt(hashMap_n.get(i).toString());
                    //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                    System.out.println("nodo a evaluar: " + nodo_evaluador);        

                    for(int j = 1; j <= hashMap_m.size(); j++){
                        String candidato = hashMap_m.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);
                        
                        recursivo(nodo_evaluador, key_origen, key_destino, contador, conector, hashMap_n, hashMap_m, nodos_descubiertos, nodos_no_descubiertos, arbol_dfs);
                        
                       
                        if(nodo_evaluador == key_origen){                        
                            if(nodos_descubiertos.containsKey(key_origen) && nodos_no_descubiertos.containsKey(key_destino)){
                                System.out.println(contador + " " + key_origen + conector+ key_destino);contador++;

                                arbol_dfs.put(contador, key_origen + conector+ key_destino);

                                nodos_no_descubiertos.remove(key_destino);
                                nodos_descubiertos.put(key_destino, key_destino);

                                System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
                                System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
                            }                    
                        }
                    }            
                }
            }
        }*/
                        
        System.out.println("ORDENAMIENTO");       
        HashMap arbol_dfs_ordenado = new HashMap();          
        contador = 1;
        for(int i = 1; i <= nodos_descubiertos.size(); i++){
            //Si existe
            if(nodos_descubiertos.containsKey(i)){
                int nodo = Integer.parseInt(nodos_descubiertos.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= arbol_dfs.size()+1; j++){
                    if(arbol_dfs.containsKey(j)){
                        String candidato = arbol_dfs.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);

                        if(nodo == key_origen){
                            //System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino);     
                            System.out.println(key_origen + conector+ key_destino);//contador++;
                            arbol_dfs_ordenado.put(contador, key_origen + conector+ key_destino);
                            contador++;
                        }
                    }                    
                }            
            }
        }              
        
        System.out.println(" arbol_bfs_ordenado.size() " + arbol_dfs_ordenado.size() + " " + arbol_dfs_ordenado.values());        
        
        System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
        System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
        
        System.out.println("*Generando archivo gexf");        
        //generar_gexf(nodos_descubiertos, arbol_dfs_ordenado, dirigido, file);
    }
    
    public static void recursivo(int contador, String conector, HashMap hashMap_n, HashMap hashMap_m, HashMap nodos_descubiertos, HashMap nodos_no_descubiertos, HashMap arbol_dfs){                                
        while(!(nodos_no_descubiertos.isEmpty())){
            System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
            System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //Si existe
                if(hashMap_n.containsKey(i)){
                    int nodo_evaluador = i; 
                    //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                    System.out.println("nodo a evaluar: " + nodo_evaluador);        

                    for(int j = 1; j <= hashMap_m.size(); j++){
                        String candidato = hashMap_m.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);
                        
                        //recursivo(nodo_evaluador, key_origen, key_destino, contador, conector, hashMap_n, hashMap_m, nodos_descubiertos, nodos_no_descubiertos, arbol_dfs);
                        
                        if(nodo_evaluador == key_origen){                        
                            if(nodos_descubiertos.containsKey(key_origen) && nodos_no_descubiertos.containsKey(key_destino)){
                                System.out.println(contador + " " + key_origen + conector+ key_destino);
                                
                                contador++;

                                arbol_dfs.put(contador, key_origen + conector+ key_destino);

                                nodos_no_descubiertos.remove(key_destino);
                                nodos_descubiertos.put(key_destino, key_destino);
                                
                                recursivo(contador, conector, hashMap_n, hashMap_m, nodos_descubiertos, nodos_no_descubiertos, arbol_dfs);
                                
                                System.out.println("Nodos descubiertos: " + nodos_descubiertos.size() +" "+ nodos_descubiertos.values());
                                System.out.println("Nodos NO descubiertos: " + nodos_no_descubiertos.size() +" "+ nodos_no_descubiertos.values());
                            }                    
                        }
                    }            
                }
            }
        }
              
    }

    /*
    public static void dijkstra(String file, HashMap hashMap_n, HashMap hashMap_m, HashMap hashMap_w, boolean dirigido){
        HashMap hashMap_pq = new HashMap();
        HashMap hashMap_pq_w = new HashMap();
        HashMap nodos_descubiertos = new HashMap();
        HashMap nodos_no_descubiertos = new HashMap();
        HashMap nodos_descubiertos_w = new HashMap();
        HashMap aristas_dijkstra = new HashMap(); 
        
        //LEE TODOS LOS NODOS
        String conector = null;
        if(dirigido == true){
            conector = "->";
        }else if(dirigido == false){
            conector = "--";
        }
        
        int contador = 1;
        for(int i = 1; i <= hashMap_n.size(); i++){
            //Si existe
            if(hashMap_n.containsKey(i)){
                int nodo = i;//Integer.parseInt(hashMap_n.get(i).toString());
                //String cond = String.valueOf(nodos_descubiertos.get(contador_capa));
                System.out.println("nodo a evaluar: " + nodo);        

                for(int j = 1; j <= hashMap_m.size(); j++){
                    String candidato = hashMap_m.get(j).toString();
                    String[] parts = candidato.split(conector);
                    Integer key_origen = Integer.parseInt(parts[0]);
                    Integer key_destino = Integer.parseInt(parts[1]);
                    double peso = Double.parseDouble(hashMap_w.get(candidato).toString());

                    if(nodo == key_origen){
                        System.out.println(contador + " Nodo adyacente a " + nodo + ": " + key_destino + " peso: " + peso);                    
                        contador++;
                    }
                }            
            }
        }
        
        //Escoger un nodo al azar dentro de los nodos existentes
        int nodo_origen = volado(1, hashMap_n.size());
            
        System.out.println("Se eligió al azar el nodo origen: " + nodo_origen);
            
        //Checar si existe el nodo origen
        if(hashMap_n.containsKey(nodo_origen)){
            System.out.println("SÍ existe el nodo (key) " + nodo_origen); 
        }
        
        int key = nodo_origen;
        String value = String.valueOf(nodo_origen);
                        
        //Intenta agregarlo
        if(nodos_descubiertos.containsValue(value)){
            System.out.println("NO se descubrió el nodo => (key) " + key + " (value) " + value);
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        //}else if(!hashMap_n.containsKey(key)){
        }else if(!nodos_descubiertos.containsValue(value)){
            //Crea el nodo con key y value
            System.out.println("Se descubrió el nodo => (key) " + key + " (value) " + value);
            nodos_descubiertos.put(key, key); 
            hashMap_pq.put(key, key); 
            hashMap_pq_w.put(key, 0.0); 
            
            for(int i = 1; i <= hashMap_n.size(); i++){
                //System.out.println("hashMap_n.get(i): " + hashMap_n.get(i));
                
                int nodo_existente = i;//Integer.parseInt(hashMap_n.get(i).toString());
                
                //System.out.println("nodo_existente: " + nodo_existente);
                if(nodo_existente != key){
                    nodos_no_descubiertos.put(nodo_existente, nodo_existente);
                }
            }
        }
        
        System.out.println("hashMap_pq: " + hashMap_pq.values());
        System.out.println("hashMap_pq_w: " + hashMap_pq_w.values());
        
        //Inicio
        
        while(!hashMap_pq.isEmpty()){        
            //System.out.println("hashMap_pq.size(): " + hashMap_pq.size()); 
            for(int k = 1; k <= hashMap_n.size(); k++){
               
                if(hashMap_pq.containsKey(k)){
                    int nodo_pq = Integer.parseInt(hashMap_pq.get(k).toString());                
                    System.out.println("nodo a evaluar en PQ: " + nodo_pq); 
                    
                    for(int j = 1; j <= hashMap_m.size(); j++){
                        String candidato = hashMap_m.get(j).toString();
                        String[] parts = candidato.split(conector);
                        Integer key_origen = Integer.parseInt(parts[0]);
                        Integer key_destino = Integer.parseInt(parts[1]);
                        double peso = Double.parseDouble(hashMap_w.get(candidato).toString());

                        //se meten a PQ los nodos adyacentes
                        if(nodo_pq == key_origen){
                            System.out.println(" Nodo adyacente a " + nodo_pq + ": " + key_destino + " peso: " + peso);
                            hashMap_pq.put(key_destino, key_destino);
                            hashMap_pq_w.put(key, peso);
                        }                                                
                    }
                                        
                    //System.out.println("hashMap_pq: " + hashMap_pq.size() + " " + hashMap_pq.values());
                    //System.out.println("hashMap_pq_w: " + hashMap_pq_w.size() + " " + hashMap_pq_w);
                    
                    //Ordenar                                      
                    HashMap map = sortByValues(hashMap_pq_w);                     
                    hashMap_pq_w = map;
                                        
                    System.out.println("hashMap_pq_w: " + hashMap_pq_w.size() + " " + hashMap_pq_w);
                    System.out.println("hashMap_pq_w ordenado: " + hashMap_pq_w.size() + " " + hashMap_pq_w);  
                    
                    
                    //System.out.println("hashMap_pq: " + hashMap_pq.size() + " " + hashMap_pq.values());
                    //System.out.println("hashMap_pq_w: " + hashMap_pq_w.size() + " " + hashMap_pq_w.values());
                                                                                
                } 
                
                   
            }                    
        }                  
    }*/
}
