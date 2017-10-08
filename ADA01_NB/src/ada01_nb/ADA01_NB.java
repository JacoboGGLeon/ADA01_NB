package ada01_nb;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


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
        
        int n = 20;
        int m = (int) ((n*n)*0.2);
        int d = (int) (n*0.2);
        
        //in in dirigido auto-ciclos
        
        //Modelo G(n,m) de Erdös y Rényi
        //erdos_renyi(n, m, true, false);
        
        //Modelo G(n,p) de Gilbert
        //gilbert(n, 0.2, true, false);
        
        //Modelo G(n,r) geográfico simple:
        geo_simple(n, 0.5, true, false);
        
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
                
        String file_bfs = "./resultados/grafos gexf/barabasi_albert_bfs" + "_" + n + "_" + d + ".gexf";
        
        bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
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
        generar_gexf(hashMap_n, hashMap_m, dirigido, file);
                
        String file_bfs = "./resultados/grafos gexf/geo_simple_bfs" + "_" + n + "_" + r + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
        
        String file_dfs = "./resultados/grafos gexf/geo_simple_dfs_i" + "_" + n + "_" + r + ".gexf";
        
        dfs_i(file_dfs, hashMap_n, hashMap_m, dirigido); 
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
        generar_gexf(hashMap_n, hashMap_m, dirigido, file);
        
        //String file_bfs = "./resultados/grafos gexf/gilbert_bfs" + "_" + n + "_" + p + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
        
        String file_dfs = "./resultados/grafos gexf/gilbert_dfs_i" + "_" + n + "_" + p + ".gexf";
        
        dfs_i(file_dfs, hashMap_n, hashMap_m, dirigido);
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

        //Se recorre el repositorio de aristas
        for(int i = 1; i <= hashMap_m.size(); i++){
            System.out.println("Arista " + i + ": " + hashMap_m.get(i));
        }
        
        //Se leen el tamaño de los repositorios de nodos y aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());
        
        System.out.println("*Generando archivo gexf");
        String file = "./resultados/grafos gexf/erdos_renyi" + "_" + n + "_" + m + ".gexf";
        generar_gexf(hashMap_n, hashMap_m, dirigido, file);
        
        //String file_bfs = "./resultados/grafos gexf/erdos_renyi_dfs_i" + "_" + n + "_" + m + ".gexf";
        
        //bfs(file_bfs, hashMap_n, hashMap_m, dirigido);
        
        String file_dfs = "./resultados/grafos gexf/erdos_renyi_dfs_i" + "_" + n + "_" + m + ".gexf";
        
        dfs_i(file_dfs, hashMap_n, hashMap_m, dirigido);
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
    
    public static void generar_gexf(HashMap hashMap_n, HashMap hashMap_m, boolean dirigido, String archivo){
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
            String[] parts = arista.split(conector);

            Integer key_origen = Integer.parseInt(parts[0]);
            Node nodo_origen = directedGraph.getNode(key_origen.toString());//gm_erdos_renyi.getGraph().getNode(key_origen);

            Integer key_destino = Integer.parseInt(parts[1]);
            Node nodo_destino = directedGraph.getNode(key_destino.toString());//gm_erdos_renyi.getGraph().getNode(key_destino);

            System.out.println("GEXF m => " + " (key) " + nodo_origen.getId().toString() + " (key) " + nodo_destino.getId().toString());

            Edge e = gm_erdos_renyi.factory().newEdge(nodo_origen, nodo_destino, dirigido);
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
        generar_gexf(nodos_descubiertos, arbol_bfs_ordenado, dirigido, file);
        
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
        generar_gexf(nodos_descubiertos, arbol_dfs_ordenado, dirigido, file);
    }
}
