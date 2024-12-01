package aed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MinHeap<T> {
    private ArrayList<T> heap;
    private int cardinal;
    private Comparator<T> comparator;

    // Constructor de heap vacío
    public MinHeap(Comparator<T> comparator) { 
        this.cardinal = 0;                  // O(1)   
        this.heap = new ArrayList<T>();     // O(1)
        this.comparator = comparator;       // O(1)
    } // Complejidad: O(1)
    
    // Constructor que toma una lista
    public MinHeap(Comparator<T> comparator, T[] lista) {
        this.cardinal = lista.length;
        this.heap = new ArrayList<T>();
        this.heap.addAll(Arrays.asList(lista));
        this.comparator = comparator;
        construirHeap();
    }
        
    public ArrayList <Integer> construirHeap() { 
        ArrayList <Integer> cambios = new ArrayList<>();
        for (int i = (cardinal - 1) / 2; i >= 0; i--) {
            reheap(i,cambios);  
        }return cambios;
    }
    public  ArrayList<Integer> obtenerPrioridades(){
        ArrayList<Integer> posiciones = construirHeap();
        return posiciones;
    }
    public ArrayList<T> elementos(){
        return this.heap;
    }
    public T obtener(int posicion){   
        return heap.get(posicion);   
    }
    public boolean isEmpty() { 
        return cardinal < 1;
    }
    
    public T getMin() { 
        if (!isEmpty()) {
            return this.heap.get(0);
        }
        return null;
    }
    
    public int getCardinal() { 
        return cardinal;
    }
    
    public ArrayList<Integer> eliminarPorPosicion(int posicion) {
        ArrayList<Integer> cambios = new ArrayList<>();
        if (esPosValida(posicion)) {
            this.heap.set(posicion, this.heap.get(cardinal - 1));
            cardinal--;
            reheap(posicion,cambios);
        }
        return cambios;
    }
    // public ArrayList<Integer> encolar(T elem) {
    //     ArrayList<Integer> cambios = new ArrayList<>();
    //     int indice = cardinal; // Nuevo elemento va al final
    //     int padre = (indice - 1) / 2;
    
    //     // Si el índice supera el tamaño del heap, agrega el elemento
    //     if (indice >= this.heap.size()) {
    //         this.heap.add(elem);
    //     } else {
    //         this.heap.set(indice, elem);
    //     }
    
    //     cambios.add(indice); // Registrar la posición inicial
    
    //     // Sift-up para restaurar la propiedad del MinHeap
    //     while (indice > 0 && comparator.compare(elem, this.heap.get(padre)) < 0) {
    //         this.heap.set(indice, this.heap.get(padre)); // Mueve el padre hacia abajo
    //         cambios.add(indice); // Registra el cambio
    //         indice = padre; // Actualiza índice al del padre
    //         padre = (indice - 1) / 2; // Recalcula el padre
    //     }
    
    //     this.heap.set(indice, elem); // Coloca el elemento en su posición final
    //     cambios.add(indice); // Registra la posición final
    
    //     cardinal++; // Incrementa el tamaño del heap
    //     return cambios;
    // }
     
    public void encolar(T elem) {
       
        int indice = cardinal;
        int padre = (indice - 1) / 2;

        while (indice > 0 && comparator.compare(elem, this.heap.get(padre)) < 0) { // Cambio de > a <
            this.heap.add(indice, this.heap.get(padre));
           // actualizarPosicion(this.heap.get(indice), indice);
            indice = padre;
            padre = (indice - 1) / 2;
        }

        if (indice >= this.heap.size()) {
            this.heap.add(indice, elem);
        } else {
            this.heap.set(indice, elem);
        }
       // actualizarPosicion(elem, indice);
        cardinal++;
    }
    
    public Tupla<T,ArrayList<Integer>> desencolar() {
        ArrayList<Integer> cambios = new ArrayList<>();
        T raiz = null;
        if (!isEmpty()) {
            raiz = this.heap.get(0);
            this.heap.set(0, this.heap.get(cardinal - 1));
            this.cardinal--;
            reheap(0,cambios);
        }
        Tupla<T,ArrayList<Integer>> res = new Tupla<>(raiz,cambios);
        return res; 
    }
    
    public void reheap(int elem,ArrayList<Integer> cambios) {
        boolean done = false;
        T temp = heap.get(elem);
        int hijo_izq = 2 * elem + 1;

        while (!done && hijo_izq < cardinal) {
            int hijo_mas_chico = hijo_izq;
            int hijo_der = hijo_izq + 1;

            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_mas_chico)) < 0) { // Cambio de > a <
                hijo_mas_chico = hijo_der;
            }

            if (comparator.compare(temp, this.heap.get(hijo_mas_chico)) > 0) { // Cambio de < a >
                this.heap.set(elem, this.heap.get(hijo_mas_chico));
                cambios.add(elem);
                //actualizarPosicion(heap.get(elem), elem);
                elem = hijo_mas_chico;
                hijo_izq = 2 * elem + 1;
            } else {
                done = true;
            }
        }

        this.heap.set(elem, temp);
        cambios.add(elem);
        //actualizarPosicion(temp, elem);
    }
    
    public void modificarEnHeap(int posicion) {
        if (esPosValida(posicion)) {
            T elemento = this.heap.get(posicion);
            siftUp(posicion);
            siftDown(posicion);
           // actualizarPosicion(elemento, posicion);
        }
    }
    
    private void siftUp(int posicion) {
        int padre = (posicion - 1) / 2;
        T elemento = this.heap.get(posicion);
        while (posicion > 0 && comparator.compare(elemento, this.heap.get(padre)) < 0) { // Cambio de > a <
            this.heap.set(posicion, this.heap.get(padre));
          //  actualizarPosicion(this.heap.get(posicion), posicion);
            posicion = padre;
            padre = (posicion - 1) / 2;
        }
        this.heap.set(posicion, elemento);
        //actualizarPosicion(elemento, posicion);
    }
    
    private void siftDown(int posicion) {
        T elemento = this.heap.get(posicion);
        int hijo_izq = 2 * posicion + 1;

        while (hijo_izq < cardinal) {
            int hijo_mas_chico = hijo_izq;
            int hijo_der = hijo_izq + 1;

            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_izq)) < 0) { // Cambio de > a <
                hijo_mas_chico = hijo_der;
            }

            if (comparator.compare(elemento, this.heap.get(hijo_mas_chico)) > 0) { // Cambio de < a >
                this.heap.set(posicion, this.heap.get(hijo_mas_chico));
              //  actualizarPosicion(this.heap.get(posicion), posicion);
                posicion = hijo_mas_chico;
                hijo_izq = 2 * posicion + 1;
            } else {
                break;
            }
        }
        this.heap.set(posicion, elemento);
        //actualizarPosicion(elemento, posicion);
    }
    
    private boolean esPosValida(int posicion) {
        return posicion >= 0 && posicion < cardinal;
    }
    
    // private void actualizarPosicion(Object elemento, int posicion) {
    //     if (elemento instanceof Traslado) {
    //         ((Traslado) elemento).setPosRedituable(posicion);
    //     } else if (elemento instanceof Ciudad) {
    //         ((Ciudad) elemento).setPosHeapSuperavit(posicion);
    //     }
    // }
    
    @Override
    public String toString() {
        if (cardinal == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cardinal; i++) {
            sb.append(this.heap.get(i));
            if (i < cardinal - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

// package aed;
// import java.util.Arrays;
// import java.util.Comparator;
// //fuente : https://docs.oracle.com/javase/8/docs/api/?java/util/Comparator.html

// public final class MinHeap<T> {
//     private T[] heap;
//     private int cardinal;
//     private Comparator<? super T> comparator;
//     private static final int cap_default = 100;
//     private boolean esHeap = false;

//  //Para crear un arreglo de tipo genérico T[] a partir de un arreglo de Object[], evitando la advertencia del compilador
//     @SuppressWarnings("unchecked")
//     public MinHeap(Comparator<? super T> comparator, int initialCapacity) {
//         this.comparator = comparator;
//         heap = (T[]) new Object[initialCapacity + 1];
//         cardinal = 0;
//         esHeap = true;
//     }

//     public MinHeap(Comparator<? super T> comparator) {
//         this(comparator, cap_default);
//         construirHeap(); 
//     }
        
//     public void construirHeap() { //Construir heap-metodo heapify-algoritmo de Floyd , por lo analziado en la teorica O(n)
        
        
//         for (int i = cardinal / 2; i > 0; i--) {
//             reheap(i);  
//         }
// }
//     public void encolar(T elem) {
//         chequearHeap();
//         int indice = cardinal + 1;
//         int padre = indice / 2;

//         while ((padre > 0) &&
//                comparator.compare(elem, heap[padre]) < 0) { 
//             heap[indice] = heap[padre];
//             if (heap[indice] instanceof Traslado) { 
//                 ((Traslado) heap[indice]).setPosHeapAntiguo(indice); 
//             }
//             indice = padre;
//             padre = indice / 2;
//         }

//         heap[indice] = elem;
//         if (elem instanceof Traslado) { 
//             ((Traslado) elem).setPosHeapAntiguo(indice); 
//         }
//         cardinal++;
//         mas_cap();
//     }
//     // O(log n), compara el elemento con su padre y lo sube.Peor caso, lo sube la altura del heap (la cual es logaritmica)

//     public T desencolar() {
//         chequearHeap();
//         T raiz = null;

//         if (!isEmpty()) {
//             raiz = heap[1];
//             heap[1] = heap[cardinal];
//             cardinal--;
//             reheap(1);
//         }

//         return raiz;
//     }
//     // O(log n) 

//     private void reheap(int elem) {
//         boolean done = false;
//         T temp = heap[elem];
//         int hijo_izq = 2 * elem;

//         // Complejidad del bucle: O(log n) = O(altura del heap)
//         while (!done && (hijo_izq <= cardinal)) {
//             int hijo_mas_chico = hijo_izq;
//             int hijo_der = hijo_izq + 1;

//             if ((hijo_der <= cardinal) &&
//                 comparator.compare(heap[hijo_der], heap[hijo_mas_chico]) < 0) { 
//                 hijo_mas_chico = hijo_der;
//             }

//             if (comparator.compare(temp, heap[hijo_mas_chico]) > 0) { 
//                 heap[elem] = heap[hijo_mas_chico];
//                 if (heap[elem] instanceof Traslado) {
//                     ((Traslado) heap[elem]).setPosHeapAntiguo(elem); 
//                 }
//                 elem = hijo_mas_chico;
//                 hijo_izq = 2 * elem;
//             } else {
//                 done = true;
//             }
//         }

//         heap[elem] = temp;
//         if (temp instanceof Traslado) {
//             ((Traslado) temp).setPosHeapAntiguo(elem); // 
//         }
//     }
//     // O(log n), pero caso elementos se mueven tanto como la altura dle heap.

//     private void mas_cap() {
//         if (cardinal >= heap.length - 1) {
//             heap = Arrays.copyOf(heap, 2 * heap.length); // O(n) porque se debe copiar todo el arreglo
//         }
//     }

//     private void chequearHeap() {
//         if (!esHeap) {
//             throw new SecurityException("Problemas con heap :(");
//         }
//     }//o(1)

//     public boolean isEmpty() {
//         return cardinal < 1;
//     }//o(1)

//     public T getMin() {
//         if (!isEmpty()) {
//             return heap[1];
//         }
//         return null;
//     } //o(1)

//     public int getCardinal() {
//         return cardinal;  // O(1) 
//     }

//     public void eliminarPorPosicion(int posicion) {
//         if (posicion <= cardinal && posicion > 0) {
//             heap[posicion] = heap[cardinal];
//             cardinal--;
//             reheap(posicion);
//         }
//     }
//     // O(log n) por reordenar el heap después de la eliminación.

//             @Override
//         public String toString() {
//             if (cardinal == 0) {
//                 return "MinHeap vacío";
//             }

//             StringBuilder sb = new StringBuilder();
//             sb.append("MinHeap: [");
//             for (int i = 1; i <= cardinal; i++) {
//                 sb.append(heap[i]);
//                 if (i < cardinal) {
//                     sb.append(", ");
//                 }
//             }
//             sb.append("]");
//             return sb.toString();
//         }


// }
