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
        
    public void construirHeap() { 
        for (int i = (cardinal - 1) / 2; i >= 0; i--) {
            reheap(i);  
        }
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
    
    public void eliminarPorPosicion(int posicion) {
        if (esPosValida(posicion)) {
            this.heap.set(posicion, this.heap.get(cardinal - 1));
            cardinal--;
            reheap(posicion);
        }
    }
    
    public void encolar(T elem) {
        int indice = cardinal;
        int padre = (indice - 1) / 2;

        while (indice > 0 && comparator.compare(elem, this.heap.get(padre)) > 0) {
            this.heap.add(indice, this.heap.get(padre));
            actualizarPosicion(this.heap.get(indice), indice);
            indice = padre;
            padre = (indice - 1) / 2;
        }

        if (indice >= this.heap.size()) {
            this.heap.add(indice, elem);
        } else {
            this.heap.set(indice, elem);
        }
        actualizarPosicion(elem, indice);
        cardinal++;
    }
    
    public T desencolar() {
        T raiz = null;
        if (!isEmpty()) {
            raiz = this.heap.get(0);
            this.heap.set(0, this.heap.get(cardinal - 1));
            this.cardinal--;
            reheap(0);
        }
        return raiz;
    }
    
    public void reheap(int elem) {
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
                actualizarPosicion(heap.get(elem), elem);
                elem = hijo_mas_grande;
                hijo_izq = 2 * elem + 1;
            } else {
                done = true;
            }
        }

        this.heap.set(elem, temp);
        actualizarPosicion(temp, elem);
    }
    
    public void modificarEnHeap(int posicion) {
        if (esPosValida(posicion)) {
            T elemento = this.heap.get(posicion);
            siftUp(posicion);
            siftDown(posicion);
            actualizarPosicion(elemento, posicion);
        }
    }
    
    private void siftUp(int posicion) {
        int padre = (posicion - 1) / 2;
        T elemento = this.heap.get(posicion);
        while (posicion > 0 && comparator.compare(elemento, this.heap.get(padre)) > 0) {
            this.heap.set(posicion, this.heap.get(padre));
            actualizarPosicion(this.heap.get(posicion), posicion);
            posicion = padre;
            padre = (posicion - 1) / 2;
        }
        this.heap.set(posicion, elemento);
        actualizarPosicion(elemento, posicion);
    }
    
    private void siftDown(int posicion) {
        T elemento = this.heap.get(posicion);
        int hijo_izq = 2 * posicion + 1;

        while (hijo_izq < cardinal) {
            int hijo_mas_grande = hijo_izq;
            int hijo_der = hijo_izq + 1;

            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_izq)) > 0) {
                hijo_mas_grande = hijo_der;
            }

            if (comparator.compare(elemento, this.heap.get(hijo_mas_grande)) < 0) {
                this.heap.set(posicion, this.heap.get(hijo_mas_grande));
                actualizarPosicion(this.heap.get(posicion), posicion);
                posicion = hijo_mas_grande;
                hijo_izq = 2 * posicion + 1;
            } else {
                break;
            }
        }
        this.heap.set(posicion, elemento);
        actualizarPosicion(elemento, posicion);
    }
    
    private boolean esPosValida(int posicion) {
        return posicion >= 0 && posicion < cardinal;
    }
    
    private void actualizarPosicion(Object elemento, int posicion) {
        if (elemento instanceof Traslado) {
            ((Traslado) elemento).setPosRedituable(posicion);
        } else if (elemento instanceof Ciudad) {
            ((Ciudad) elemento).setPosHeapSuperavit(posicion);
        }
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

