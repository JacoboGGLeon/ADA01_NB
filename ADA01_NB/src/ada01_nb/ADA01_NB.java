package ada01_nb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


public class ADA01_NB {
    
    public static void main(String[] args) {
        System.out.println("Graphs");
        
        //Modelo G(n,m) de Erdös y Rényi
        erdos_renyi(100, 30, true);
        
        //Graph toolkit API
        //test_graphs();
        

    }

    public static int volado(int min, int max){
        return (int) Math.floor(Math.random()*(max-min+1)+(min));
    }

    /*
        Modelo G(n,m) de Erdös y Rényi:
        1. crear n vértices
        2. elegir uniformemente al azar m distintos pares de distintos vértices
    */

    public static void erdos_renyi(int n, int m, boolean dirigido){
        HashMap hashMap_n = new HashMap();
        HashMap hashMap_m = new HashMap();
        int aristas_totales = 0, aristas = 0;
        
        /*
            Es necesario crear un proyecto para usar las características del kit de 
            herramientas. La creación de un nuevo proyecto también crea un espacio 
            de trabajo.
        */
        
        //Init a project - and therefore a workspace
        ProjectController pc_erdos_renyi = Lookup.getDefault().lookup(ProjectController.class);
        pc_erdos_renyi.newProject();
        Workspace ws_erdos_renyi = pc_erdos_renyi.getCurrentWorkspace();
        
        //Get a graph model - it exists because we have a workspace
        GraphModel gm_erdos_renyi = Lookup.getDefault().lookup(GraphController.class).getGraphModel(ws_erdos_renyi);
        
        //Append as a Undirected Graph
        DirectedGraph directedGraph = gm_erdos_renyi.getDirectedGraph();
        
        //Gnerar n vértices
        for(int v = 1; v <= n; v++){
            boolean respuesta = crear_nodo(v, hashMap_n);
            
            //Crear nodos
            if(respuesta == true){
                String nodo = Integer.toString(v);
                
                //Node[] neighbors = gm_erdos_renyi.factory().newNode(v);
                Node n_nodo = gm_erdos_renyi.factory().newNode(nodo);
                n_nodo.setLabel("Nodo " + nodo);
                
                //Append
                directedGraph.addNode(n_nodo); 
               
            }
        }
        
        //Generar m aristas
        while(aristas_totales != m){
            //elegir un nodo al azar
            int nodo_al_azar_1 = volado(1, n);
            int nodo_al_azar_2 = volado(1, n);
                        
            //System.out.println("Se eligió al azar el nodo " + nodo_al_azar_1);
            //System.out.println("Se eligió al azar el nodo " + nodo_al_azar_2);

            boolean respuesta = crear_arista(nodo_al_azar_1, nodo_al_azar_2, aristas, hashMap_m);

            if(respuesta){
                aristas++;
                aristas_totales++;
                
                Node n_nodo_01 = gm_erdos_renyi.factory().newNode("0"); 
                Node n_nodo_02 = gm_erdos_renyi.factory().newNode("0");
                
                
                //Iterate over nodes
                for (Node n_nodo_temp : directedGraph.getNodes()) {
                    if(Integer.parseInt(n_nodo_temp.getId().toString()) == nodo_al_azar_1){
                        n_nodo_01 = directedGraph.getNode(n_nodo_temp.getId());
                        
                        System.out.println("Nodo al azar: " + nodo_al_azar_1);
                        System.out.println("Nuevo nodo => label: " + n_nodo_temp.getLabel() + " id: " + n_nodo_temp.getId());
                        
                        //break;
                    }else if(Integer.parseInt(n_nodo_temp.getId().toString()) == nodo_al_azar_2){
                        n_nodo_02 = directedGraph.getNode(n_nodo_temp.getId());
                        
                        System.out.println("Nodo al azar: " + nodo_al_azar_2);
                        System.out.println("Nuevo nodo => label: " + n_nodo_temp.getLabel() + " id: " + n_nodo_temp.getId());
                        
                        //break;
                    }
                }
                
                System.out.println("Arista: " + n_nodo_01.getId().toString() + " + " + n_nodo_02.getId().toString());
                
                //Create an edge - directed and weight 1
                Edge e_edge = gm_erdos_renyi.factory().newEdge(n_nodo_01, n_nodo_02, false);
                directedGraph.addEdge(e_edge);
                
            }

        }

        //leer todas las aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());

        for(int i = 1; i <= hashMap_m.size(); i++){
            //System.out.println("Arista " + i + ": " + hashMap_m.get(i));
            System.out.println(hashMap_m.get(i));
        }
        
        //Export full graph
        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);
        
        try{
            exportController.exportFile(new File("text.gexf"), ws_erdos_renyi);
            System.out.println("Se creó el archivo gexf");
        }catch(IOException e){
            System.out.println(e);
        }


    }

    public static boolean crear_nodo(int key, HashMap hashMap_n){
        boolean respuesta = false;

        // checa el hashmap
        // si existe el key del nodo, entonces no lo agrega al hashmap
        if(hashMap_n.containsValue(key)){
            respuesta = false;
            //System.out.println("NO Se creó el nodo: " + hashMap_n.get(key));
        // si no existe el key del nodo, entonces sí lo agrega al hashmap
        }else if(!hashMap_n.containsValue(key)){
            respuesta = true;
            hashMap_n.put(key, key);
            //System.out.println("Se creó el nodo: " + hashMap_n.get(key));
        }

        return respuesta;
    }

    public static boolean crear_arista(int key_nodo_u, int key_nodo_v, int key, HashMap hashMap_m){
        boolean respuesta = false;
        String arista = key_nodo_u + "--" + key_nodo_v;

        // checa el hashmap
        // si existe el key de la arista no lo agrega al hashmap
        if(hashMap_m.containsValue(arista)){
            respuesta = false;

        // si no existe el key de la arista entonces sí lo agrega al hashmap
        }else if(!hashMap_m.containsValue(arista)){
            hashMap_m.put(key, arista);
            //System.out.println("Se creó la arista: " + hashMap_m.get(key));
            respuesta = true;
        }

        return respuesta;
    }
    
    public static void test_graphs() {
        /*
        Es necesario crear un proyecto para usar las características del kit de 
        herramientas. La creación de un nuevo proyecto también crea un espacio 
        de trabajo.
        */
        
        //Init a project - and therefore a workspace
        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        projectController.newProject();
        
        Workspace workspace = projectController.getCurrentWorkspace();
        
        //Create two nodes
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        Node n0 = graphModel.factory().newNode("n0");
        n0.setLabel("Nodo n0");
        
        Node n1 = graphModel.factory().newNode("n1");
        n1.setLabel("Nodo 01");
        
        //Create an edge - directed and weight 1
        Edge e1 = graphModel.factory().newEdge(n0,n1, 1, true);
        
        //Append as a Directed Graph
        DirectedGraph directedGraph = graphModel.getDirectedGraph();
        directedGraph.addNode(n0);
        directedGraph.addNode(n1);
        directedGraph.addEdge(e1);
        
        //Export full graph
        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);
        
        try{
            exportController.exportFile(new File("text.gexf"), workspace);
            System.out.println("Se creó el archivo gexf");
        }catch(IOException e){
            System.out.println(e);
        }
            
    }
}
