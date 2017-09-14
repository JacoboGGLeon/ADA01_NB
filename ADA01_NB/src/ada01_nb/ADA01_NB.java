/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ada01_nb;

import java.util.HashMap;

public class ADA01_NB {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Modelo G(n,m) de Erdös y Rényi
        erdos_renyi(50, 40);

    }

    private static int volado(int min, int max){
        return (int) Math.floor(Math.random()*(max-min+1)+(min));
    }

    /*
        Modelo G(n,m) de Erdös y Rényi:
        1. crear n vértices
        2. elegir uniformemente al azar m distintos pares de distintos vértices
    */

    private static void erdos_renyi(int n, int m){
        HashMap hashMap_n = new HashMap();
        HashMap hashMap_m = new HashMap();
        int aristas_totales = 0, aristas = 1;

        //generar n vértices
        for(int v = 1; v <= n; v++){
            boolean respuesta = crear_nodo(v, hashMap_n);
        }

        //generar m aristas
        while(aristas_totales != m){
            //elegir un nodo al azar
            int nodo_al_azar_1 = volado(1, n);
            int nodo_al_azar_2 = volado(1, n);

            System.out.println("Se eligió al azar el nodo " + nodo_al_azar_1);
            System.out.println("Se eligió al azar el nodo " + nodo_al_azar_2);

            boolean respuesta = crear_arista(nodo_al_azar_1, nodo_al_azar_2, aristas, hashMap_m);

            if(respuesta){
                aristas++;
                aristas_totales++;
            }

        }

        //leer todas las aristas
        System.out.println("Nodos: " + hashMap_n.size());
        System.out.println("Aristas: " + hashMap_m.size());

        for(int i = 1; i <= hashMap_m.size(); i++){
            System.out.println("Arista " + i + ": " + hashMap_m.get(i));
        }


    }

    private static boolean crear_nodo(int key, HashMap hashMap_n){
        boolean respuesta = false;

        // checa el hashmap
        // si existe el key del nodo, entonces no lo agrega al hashmap
        if(hashMap_n.containsValue(key)){
            respuesta = false;
            System.out.println("NO Se creó el nodo: " + hashMap_n.get(key));
        // si no existe el key del nodo, entonces sí lo agrega al hashmap
        }else if(!hashMap_n.containsValue(key)){
            respuesta = true;
            hashMap_n.put(key, key);
            System.out.println("Se creó el nodo: " + hashMap_n.get(key));
        }

        return respuesta;
    }

    private static boolean crear_arista(int key_nodo_u, int key_nodo_v, int key, HashMap hashMap_m){
        boolean respuesta = false;
        String arista = key_nodo_u + "--" + key_nodo_v;

        // checa el hashmap
        // si existe el key de la arista no lo agrega al hashmap
        if(hashMap_m.containsValue(arista)){
            respuesta = false;

        // si no existe el key de la arista entonces sí lo agrega al hashmap
        }else if(!hashMap_m.containsValue(arista)){
            hashMap_m.put(key, arista);
            System.out.println("Se creó la arista: " + hashMap_m.get(key));
            respuesta = true;
        }

        return respuesta;
    }
}
