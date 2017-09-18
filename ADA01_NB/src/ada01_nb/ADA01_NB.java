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
        
        //in in dirigido auto-ciclos
        
        //Modelo G(n,m) de Erdös y Rényi
        //erdos_renyi(50, 300, true, true);
        
        //Modelo G(n,p) de Gilbert
        gilbert(500, 0.2, true, false);

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
            boolean respuesta = crear_nodo(v, hashMap_n);
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
                
                System.out.println("ARISTAS DEL NODO " + i + aristas_por_nodo);
            }
        
        /*
        //Se recorre el repositorio de aristas
        for(int i = 1; i <= hashMap_m.size(); i++){
            System.out.println("Arista " + i + ": " + hashMap_m.get(i));
        }
        */
        
        //Se leen el tamaño de los repositorios de nodos y aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());
        
        System.out.println("*Generando archivo gexf");
        generar_gexf(hashMap_n, hashMap_m, dirigido, "gilbert.gexf");
        
        
        /*
        //Generar m aristas
        while(aristas_totales != ((valor_esperado*n))){
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

            System.out.println("Se intenta crear el arista " + nodo_origen + " + " + nodo_destino);
            boolean respuesta = crear_arista(nodo_origen, nodo_destino, aristas, hashMap_m, dirigido, auto);

            //Si se creó el arista, continua 
                if(respuesta){
                    aristas++;
                    aristas_totales++;
                }
            }

        */
 
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
            boolean respuesta = crear_nodo(v, hashMap_n);
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
        generar_gexf(hashMap_n, hashMap_m, dirigido, "erdos_renyi.gexf");
    }
    
    public static boolean crear_nodo(int key, HashMap hashMap_n){
        boolean respuesta = false;

        // checa el hashmap
        // Si ya existe el nodo, entonces NO lo agrega al repositorio
        if(hashMap_n.containsKey(key)){
            System.out.println("NO se creó el nodo => (key) " + hashMap_n.get(key) + " (value) " + hashMap_n.values());
            //Regresa falso
            respuesta = false;
            
        // Si NO existe el nodo, entonces SÍ lo agrega al repositorio
        }else if(!hashMap_n.containsKey(key)){
            //Crea el nodo con key y value
            hashMap_n.put(key, key);
            System.out.println("Se creó el nodo => (key) " + key + " (value) " + hashMap_n.get(key));
            
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
            
            Node n = gm_erdos_renyi.factory().newNode(hashMap_n.get(i).toString());
            n.setLabel(hashMap_n.get(i).toString());
            n.setColor(Color.decode("#ff99dc"));
            
            System.out.println("GEXF n => " + "(key) " + hashMap_n.get(i) + " (value) " + n.getLabel());
                
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
        
        
        //Export full graph
        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);
        
        try{
            exportController.exportFile(new File(archivo), ws_erdos_renyi);
            System.out.println("Se creó el archivo: " + archivo);
        }catch(IOException e){
            System.out.println(e);
        }
        
    }
    
}
