package aed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MaxHeap<T> {
    private ArrayList<T> heap;
    private int cardinal; 
    private Comparator<T> comparator;

    // Constructor de heap vacío
    public MaxHeap(Comparator<T> comparator) { 
        this.cardinal = 0;                  // O(1)   
        this.heap = new ArrayList<T>();     // O(1)
        this.comparator = comparator;   // O(1)
    } // Complejidad: O(1)
    
    // Constructor que toma una lista
    public MaxHeap(Comparator<T> comparator, T[] lista) {
        this.cardinal = lista.length;
        this.heap = new ArrayList<T>();
        this.heap.addAll(Arrays.asList(lista));
        this.comparator = comparator;
        construirHeap();
    }
        
    public  ArrayList<Integer> construirHeap() { 
        ArrayList<Integer> cambios = new ArrayList<>();
        for (int i = (cardinal - 1) / 2; i >= 0; i--) {
            reheap(i,cambios);  
        }
        return cambios;
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
    
    public T getMax() { 
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
    //     int indice = cardinal;
    //     int padre = (indice - 1) / 2;
    
    //     if (indice >= this.heap.size()) {
    //         this.heap.add(elem); // Si el índice supera el tamaño actual, agregar directamente.
    //     } else {
    //         this.heap.set(indice, elem); // Si no, reemplazar en el índice.
    //     }
    
    //     cambios.add(indice); // Registrar la posición inicial.
    
    //     while (indice > 0 && comparator.compare(elem, this.heap.get(padre)) > 0) {
    //         this.heap.set(indice, this.heap.get(padre)); // Mover el elemento del padre hacia abajo.
    //         cambios.add(indice); // Registrar el cambio.
    //         indice = padre;
    //         padre = (indice - 1) / 2;
    //     }
    
    //     this.heap.set(indice, elem); // Colocar el nuevo elemento en su posición final.
    //     cambios.add(indice); // Registrar la posición final.
    
    //     cardinal++;
    //     return cambios;
    // }
    
    public void encolar(T elem) {
        ArrayList<Integer> cambios = new ArrayList<>();
        int indice = cardinal;
        int padre = (indice - 1) / 2;

        while (indice > 0 && comparator.compare(elem, this.heap.get(padre)) > 0) {
            this.heap.add(indice, this.heap.get(padre));
          //  actualizarPosicion(this.heap.get(indice), indice);
            
          indice = padre;
            padre = (indice - 1) / 2;
        }

        if (indice >= this.heap.size()) {
            this.heap.add(indice, elem);
        } else {
            this.heap.set(indice, elem);
        }
        //actualizarPosicion(elem, indice);
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
        return res; //retorno lista de cambios para no romper encapsulamiento 
    }
    
    public void reheap(int elem,ArrayList<Integer> cambios) {
        boolean done = false;
        T temp = heap.get(elem);
        int hijo_izq = 2 * elem + 1;

        while (!done && hijo_izq < cardinal) {
            int hijo_mas_grande = hijo_izq;
            int hijo_der = hijo_izq + 1;

            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_mas_grande)) > 0) {
                hijo_mas_grande = hijo_der;
            }

            if (comparator.compare(temp, this.heap.get(hijo_mas_grande)) < 0) {
                this.heap.set(elem, this.heap.get(hijo_mas_grande));
                cambios.add(elem);
               // actualizarPosicion(heap.get(elem), elem);
                elem = hijo_mas_grande;
                hijo_izq = 2 * elem + 1;
            } else {
                done = true;
            }
        }

        this.heap.set(elem, temp);
        cambios.add(elem);
        //actualizarPosicion(temp, elem);
    }
    public ArrayList<Integer> modificarEnHeap(int posicion) {
        // Complejidad: O(log n)
        ArrayList<Integer> cambios = new ArrayList<>();
        if (esPosValida(posicion)) { // Complejidad: O(1)
            T elemento = this.heap.get(posicion); // O(1)
            siftUp(posicion, cambios); // O(log n) en el peor caso
            siftDown(posicion, cambios); // O(log n) en el peor caso
            // actualizarPosicion(elemento, posicion); // Si aplica, depende de su implementación
        }
        return cambios; // Complejidad: O(1)
    }
    
    private void siftUp(int posicion, ArrayList<Integer> cambios) {
        // Complejidad: O(log n)
        int padre = (posicion - 1) / 2; // O(1)
        T elemento = this.heap.get(posicion); // O(1)
        while (posicion > 0 && comparator.compare(elemento, this.heap.get(padre)) > 0) { // O(log n)
            this.heap.set(posicion, this.heap.get(padre)); // O(1)
            cambios.add(posicion); // O(1)
            // actualizarPosicion(this.heap.get(posicion), posicion); // Si aplica, depende de su implementación
            posicion = padre; // O(1)
            padre = (posicion - 1) / 2; // O(1)
        }
        this.heap.set(posicion, elemento); // O(1)
        cambios.add(posicion); // O(1)
        // actualizarPosicion(elemento, posicion); // Si aplica, depende de su implementación
    }
    
    private void siftDown(int posicion, ArrayList<Integer> cambios) {
        // Complejidad: O(log n)
        T elemento = this.heap.get(posicion); // O(1)
        int hijo_izq = 2 * posicion + 1; // O(1)
    
        while (hijo_izq < cardinal) { // O(log n)
            int hijo_mas_grande = hijo_izq; // O(1)
            int hijo_der = hijo_izq + 1; // O(1)
    
            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_izq)) > 0) { // O(1)
                hijo_mas_grande = hijo_der; // O(1)
            }
    
            if (comparator.compare(elemento, this.heap.get(hijo_mas_grande)) < 0) { // O(1)
                this.heap.set(posicion, this.heap.get(hijo_mas_grande)); // O(1)
                cambios.add(posicion); // O(1)
                // actualizarPosicion(this.heap.get(posicion), posicion); // Si aplica, depende de su implementación
                posicion = hijo_mas_grande; // O(1)
                hijo_izq = 2 * posicion + 1; // O(1)
            } else {
                break; // O(1)
            }
        }
        this.heap.set(posicion, elemento); // O(1)
        cambios.add(posicion); // O(1)
        // actualizarPosicion(elemento, posicion); // Si aplica, depende de su implementación
    }
    
    
    // public void modificarEnHeap(int posicion) {
    //     if (esPosValida(posicion)) {
    //         T elemento = this.heap.get(posicion);
    //         siftUp(posicion);
    //         siftDown(posicion);
    //         //actualizarPosicion(elemento, posicion);
    //     }
    // }
    
    // private void siftUp(int posicion) {
    //     int padre = (posicion - 1) / 2;
    //     T elemento = this.heap.get(posicion);
    //     while (posicion > 0 && comparator.compare(elemento, this.heap.get(padre)) > 0) {
    //         this.heap.set(posicion, this.heap.get(padre));
    //       //  actualizarPosicion(this.heap.get(posicion), posicion);
    //         posicion = padre;
    //         padre = (posicion - 1) / 2;
    //     }
    //     this.heap.set(posicion, elemento);
    //     //actualizarPosicion(elemento, posicion);
    // }
    
    // private void siftDown(int posicion) {
    //     T elemento = this.heap.get(posicion);
    //     int hijo_izq = 2 * posicion + 1;

    //     while (hijo_izq < cardinal) {
    //         int hijo_mas_grande = hijo_izq;
    //         int hijo_der = hijo_izq + 1;

    //         if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_izq)) > 0) {
    //             hijo_mas_grande = hijo_der;
    //         }

    //         if (comparator.compare(elemento, this.heap.get(hijo_mas_grande)) < 0) {
    //             this.heap.set(posicion, this.heap.get(hijo_mas_grande));
    //            // actualizarPosicion(this.heap.get(posicion), posicion);
    //             posicion = hijo_mas_grande;
    //             hijo_izq = 2 * posicion + 1;
    //         } else {
    //             break;
    //         }
    //     }
    //     this.heap.set(posicion, elemento);
    // //    actualizarPosicion(elemento, posicion);
    // }
    
    
    // // private void actualizarPosicion(Object elemento, int posicion) {
    //     if (elemento instanceof Traslado) {
    //         ((Traslado) elemento).setPosRedituable(posicion);
    //     } else if (elemento instanceof Ciudad) {
    //         ((Ciudad) elemento).setPosHeapSuperavit(posicion);
    //     }
    // }
    private boolean esPosValida(int posicion) {
        return posicion >= 0 && posicion < cardinal;
    }
    
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

