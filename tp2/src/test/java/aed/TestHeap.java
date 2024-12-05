package aed;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestHeap {

    private Comparator<Integer> comparator;

    @BeforeEach
    void setUp() {
        comparator = Integer::compare;
    }

    @Test
    void heap_Vacio() {
        Heap<Integer> heap = new Heap<>(comparator);
        assertTrue(heap.isEmpty(), "Al iniciar el heap,debe estar vacio");
        assertEquals(0, heap.getCardinal(), "El cardinal deberia ser cero");
    }

    @Test
    void encolar_Elementos() {
        Heap<Integer> heap = new Heap<>(comparator);
        heap.encolar(100);
        heap.encolar(200);
        heap.encolar(150);
        assertEquals(3, heap.getCardinal(), "El cardinal deberia ser 3");
        assertEquals(200, heap.getPrimero(), "El primero elemento deberia ser 200");
    }

    @Test
    void desencolar_Maximo() {
        Heap<Integer> heap = new Heap<>(comparator);
        heap.encolar(100);
        heap.encolar(200);
        heap.encolar(150);
        Tupla<Integer, ArrayList<Integer>> resultado = heap.desencolar();
        assertEquals(200, resultado.getPrimero(), "El elemento deberia ser 200");
        assertEquals(150, heap.getPrimero(), "Luego de desencolar, el primero deberia ser 150");
        assertEquals(2, heap.getCardinal(), "Cardinal deberia ser 2");
    }

    @Test
    void heap_a_partir_de_array() {
        Integer[] lista = {30, 40, 20, 10};
        Heap<Integer> heapConLista = new Heap<>(comparator, lista);
        assertEquals(4, heapConLista.getCardinal(), "Cardinal deberia ser 4");
        assertEquals(40, heapConLista.getPrimero(), "El primer elemento deberia ser 40");
    }

    @Test
    void eliminar_por_posicion() {
        Integer[] lista = {30, 40, 20, 10};
        Heap<Integer> heapConLista = new Heap<>(comparator, lista);
        ArrayList<Integer> cambios = heapConLista.eliminarPorPosicion(1); // elimino 30
        Tupla<Integer, ArrayList<Integer>> resultado = heapConLista.desencolar(); //el maximo sigue siendo 40, desencolo y veo primer posicion
        assertTrue(!heapConLista.getPrimero().equals(30), "Luego de eliminar, el primero no deberia ser 30,porque ya lo eliminamos antes con eliminarPorPosicion");
        assertEquals(2, heapConLista.getCardinal(), "el cardinal deberia ser 2");
    }

    @Test
    void modificar_en_heap() {
        Integer[] lista = {310, 490, 488, 10};
        Heap<Integer> heapConLista = new Heap<>(comparator, lista);
        heapConLista.encolar(500);
        ArrayList<Integer> cambiosMod = heapConLista.modificarEnHeap(2); // modifico posicion arbitraria
        assertEquals(500, heapConLista.getPrimero(), "despues de modficar, el maximo debe ser 500");
    }
}
