package aed;
/*Materia : Algoritmos y Estructuras de datos 2
 * Segundo cuatrimestre 2024 
 * Reentrega trabajo practico
 * Grupo BYMRO.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Heap<T> {
    private ArrayList<T> heap;
    private int cardinal; 
    private Comparator<T> comparator;

    // Constructor de heap vacío
    public Heap(Comparator<T> comparator) { 
        this.cardinal = 0;                  // O(1)   
        this.heap = new ArrayList<T>();     // O(1)
        this.comparator = comparator;   // O(1)
    } // Complejidad: O(1)
    
    // Constructor que toma una lista
    public Heap(Comparator<T> comparator, T[] lista) {
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
        
        if (!esPosValida(posicion)) {
            return cambios; 
        }
        
        if (posicion == this.cardinal - 1) {
            borrarUltimo();
            return cambios; 
        }
    
        this.heap.set(posicion, this.heap.get(cardinal - 1));
        cardinal--;
        reheap(posicion, cambios);
        this.heap.remove(cardinal);
    
        return cambios;
    }
    
    
   
    public void borrarUltimo(){
        this.heap.remove(cardinal-1);
        this.cardinal--;
    }
    
    public ArrayList<Integer> encolar(T elem) {
        ArrayList<Integer> cambios = new ArrayList<>();
        if (cardinal >= heap.size()) {
            heap.add(elem); 
        } else {
            heap.set(cardinal, elem); 
        }
        siftUp(cardinal, cambios); 
        cardinal++;
        return cambios; 
    }
    
    public Tupla<T,ArrayList<Integer>> desencolar() {
        ArrayList<Integer> cambios = new ArrayList<>();
        T raiz = null;
        if (this.cardinal > 1) {
            raiz = this.heap.get(0);
            this.heap.set(0, this.heap.get(cardinal - 1));
            this.heap.remove(cardinal-1);
            this.cardinal--;
            reheap(0,cambios);
        }else if (this.cardinal > 0) {
            raiz = this.heap.get(0);
            this.heap.remove(cardinal-1);
            this.cardinal--;
           
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
              
                elem = hijo_mas_grande;
                hijo_izq = 2 * elem + 1;
            } else {
                done = true;
            }
        }

        this.heap.set(elem, temp);
        cambios.add(elem);
        
    }

    
    public ArrayList<Integer> modificarEnHeap(int posicion) {
        // Complejidad: O(log n)
        ArrayList<Integer> cambios = new ArrayList<>();
        if (esPosValida(posicion)) { // Complejidad: O(1)
            T elemento = this.heap.get(posicion); // O(1)
            siftUp(posicion, cambios); // O(log n) en el peor caso
            siftDown(posicion, cambios); // O(log n) en el peor caso
        }
        return cambios; // Complejidad: O(1)
    }
    
    private void siftUp(int posicion, ArrayList<Integer> cambios) {
        //Propósito: Garantizar que el heap preserve la propiedad del máximo mientras sube el nuevo elemento a su posición correcta.
        // Complejidad: O(log n)
        int padre = (posicion - 1) / 2; // O(1)
        T elemento = this.heap.get(posicion); // O(1)
        while (posicion > 0 && comparator.compare(elemento, this.heap.get(padre)) > 0) { // O(log n)
            this.heap.set(posicion, this.heap.get(padre)); // O(1)
            cambios.add(posicion); // O(1)
            posicion = padre; // O(1)
            padre = (posicion - 1) / 2; // O(1)
        }
        this.heap.set(posicion, elemento); // O(1)
        cambios.add(posicion); // O(1)
    }
    
    private void siftDown(int posicion, ArrayList<Integer> cambios) {
        //Propósito: Asegurar que un elemento en una posición inicial dada descienda hasta donde preserve la propiedad del heap.
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
                posicion = hijo_mas_grande; // O(1)
                hijo_izq = 2 * posicion + 1; // O(1)
            } else {
                break; // O(1)
            }
        }
        this.heap.set(posicion, elemento); // O(1)
        cambios.add(posicion); // O(1)
    }
    
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
   
