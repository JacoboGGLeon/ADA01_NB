package ada01_nb;

//import java.io.File;
//import java.io.IOException;
import java.util.HashMap;

/*
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
*/

public class ADA01_NB {
    
    public static void main(String[] args) {
        System.out.println("Graphs");
        
        //Modelo G(n,m) de Erdös y Rényi
        erdos_renyi(10, 30, true, false);

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
            String value = "Nodo: " + key;
            hashMap_n.put(key, value);
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
    
    
    
}
