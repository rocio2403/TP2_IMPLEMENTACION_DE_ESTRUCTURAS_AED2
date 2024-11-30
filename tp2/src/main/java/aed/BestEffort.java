package aed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

public class BestEffort {
   // private ArrayList<Ciudad> ciudades;
    private Ciudad[] ciudades;  //ahora uso un array porque la cantidad de ciudades es fija (y es ma linda la notacion)
    public  MaxHeap<Traslado> heapRedituable; 
    public MinHeap<Traslado> heapAntiguo;    
    private ArrayList<Integer> mayorGanancia; 
    private  ArrayList<Integer> mayorPerdida;  
    private MaxHeap<Ciudad> heapSuperavit;
    private  int mayorSuperavit;
    private int cantGanancia; 
    private int cantTraslados;

    public BestEffort(int cantCiudades, Traslado[] traslados) {
        // this.ciudades = new ArrayList<>(cantCiudades);
        // for (int i = 0; i < cantCiudades; i++) {
        //     ciudades.add(new Ciudad(i, 0, 0, 0)); 
        // } //O(|C|)
        this.ciudades = new Ciudad[cantCiudades];
        for(int i = 0;i<cantCiudades;i++){
            ciudades[i] = new Ciudad(i, 0, 0, 0);
        }

        Comparator<Traslado> comparadorPorGN =Comparator.comparing(Traslado::getGananciaNeta);
        this.heapRedituable = new MaxHeap<>(comparadorPorGN, traslados); //O(|T|)
        Comparator<Traslado> timeStampComparar =Comparator.comparing(Traslado::getTimestamp);
         this.heapAntiguo = new MinHeap<>(timeStampComparar, traslados); //O(|T|)
        Comparator<Ciudad> comparadorPorSuperavit = Comparator.comparing(Ciudad::getSuperavit);
        this.heapSuperavit = new MaxHeap<>(comparadorPorSuperavit, ciudades);//O(|C|)
          
        this.mayorSuperavit = 0;
        this.mayorGanancia = new ArrayList<Integer>(); 
        this.mayorPerdida = new ArrayList<Integer>();
        
        this.cantGanancia = 0;
        this.cantTraslados = 0;
       
    }// Complejidad final = O(|C|+ |T|)

    public void registrarTraslados(Traslado[] traslados){
        for (Traslado traslado : traslados){  //O(|traslados|)
            this.heapRedituable.encolar(traslado); //O(log(T))
            this.heapAntiguo.encolar(traslado); //O(log(T))
        } 
    } //complejidad: O(|traslados|log(t))

   

    public int[] despacharMasRedituables(int n){
        int i = 0;
        int[] res = new int[n];  //O(n)
        int[] indices = new int[n];//O(n)

        while (i < n && heapRedituable.getCardinal() > 0){
            Traslado t = heapRedituable.desencolar();  //O(log(T))
            res[i] = t.getId();    
            actualizarInfoCiudad(t);    //O(Log(C))   
           indices[i] =t.getPosAntiguo();
            i++;
            
        } //Complejidad bucle = O(n(log(T) + log(C)))
       
         sincronizarHeapAntiguo(indices); //o(nlog(T))
        actualizarListaGananciasYPerdidas(); //O(|C|)
        
        
        return res ;
       //Complejidad = O(|C|) + O(n(Log(T)) + O(n(Log(T) + log(C))) =  O(n(logT + logC))
    }

    public int[] despacharMasAntiguos(int n){
        int i = 0;
        int[] res = new int[n]; //renombrar
        int[] indices = new int[n];
        while (i< n && heapAntiguo.getCardinal() > 0){
            Traslado t  = this.heapAntiguo.desencolar(); // O(log(T))
              // O(1)
            indices[i]=t.getPosRedituable();
            
            res[i] = t.getId();    
            actualizarInfoCiudad(t);    //O(Log(C)) 
            i++;
           
        }   //Complejidad bucle = O(n(log(T) + log(C)))
     
        sincronizarHeapRedituable(indices);
        actualizarListaGananciasYPerdidas(); //O(|C|)
       
        
        return res ;
    } //Complejidad = O(|C|) + O(n(Log(T) + log(C))) =  O(n(logT + logC))

    
    
    public int ciudadConMayorSuperavit(){
        return mayorSuperavit;//O(1)
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
         return mayorGanancia; //O(1)
        }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        
        return mayorPerdida;//O(1)
    }

    public int gananciaPromedioPorTraslado() {
        if (cantTraslados == 0) {
            return 0; 
        }
        return cantGanancia / cantTraslados; //O(1)
    }
    
    /* ====================================================================================
     *                  FUNCIONES AUXILIARES
     * ====================================================================================
    */

    //CAMBIO PRINCIPAL. EN VEZ DE RECORRER TODO, LO HAGO CADA VEZ QUE AGREGO UN TRASLADO Y MIRO SOLO LAS CIUDADES AFECTADAS POR ESE TRASLADO
    // private void actualizarMayorGanancia(Traslado t){
    //     /// separo en tres opciones:
    //     /// si la lista no esta vacia, como todos tienen la misma ganancia,comparo, si es mayor .clear(O(1)), si es menor, no hago nada, si es igual agrego.
    //     /// 
    //     int idCiudadGanancia = t.getOrigen();
    //     //primeor actualizo la info de ciudades en la otra funcion
    //     int ganancia = ciudades[idCiudadGanancia].getGanancias();
    //     // ahora actualizo la lista de acuerdo a los casos
    //     if(this.mayorGanancia.size() == 0){
    //         this.mayorGanancia.add(idCiudadGanancia);
    //     }else{
    //         int maxGanancia = this.mayorGanancia.get(0);
    //         int mayorGananciaActual = this.ciudades[maxGanancia].getGanancias();
    //         if (mayorGananciaActual < ganancia) {
    //             this.mayorGanancia.clear();
    //             this.mayorGanancia.add(idCiudadGanancia);
    //         }
    //         else if (mayorGananciaActual == ganancia) {
    //             this.mayorGanancia.add(idCiudadGanancia);
    //         }
    //     }
    // }

    // private void actualizarMayorPerdida(Traslado t){
    //     int idCiudadPerdida = t.getDestino();
    //     //primeor actualizo la info de ciudades en la otra funcion
    //     int perdida = ciudades[idCiudadPerdida].getPerdidas();
    //     // ahora actualizo la lista de acuerdo a los casos
    //     if(this.mayorPerdida.size() == 0){
    //         this.mayorPerdida.add(idCiudadPerdida);
    //     }else{
    //         int maxPerdida = this.mayorPerdida.get(0);
    //         int mayorPerdidaActual = this.ciudades[maxPerdida].getPerdidas();
    //         if (mayorPerdidaActual < perdida) {
    //             this.mayorPerdida.clear();
    //             this.mayorPerdida.add(idCiudadPerdida);
    //         }
    //         else if (mayorPerdidaActual == perdida) {
    //             this.mayorGanancia.add(idCiudadPerdida);
    //         }
    //     }
    // }
    public void actualizarListaGananciasYPerdidas() { 
        int mayorGananciaActual = -1; 
        int mayorPerdidaActual = -1;
        mayorGanancia.clear();
        mayorPerdida.clear();
        if (!mayorGanancia.isEmpty()) {
            int idCiudadGanancia = mayorGanancia.get(0);
            mayorGananciaActual = ciudades[idCiudadGanancia].getGanancias();
        }
        if (!mayorPerdida.isEmpty()) {
            int idCiudadPerdida = mayorPerdida.get(0);
            mayorPerdidaActual = ciudades[idCiudadPerdida].getPerdidas();
        }
    
        for (Ciudad ciudad : ciudades) { // O(C)
            int ganancia = ciudad.getGanancias();
            if (ganancia > mayorGananciaActual) {
                mayorGananciaActual = ganancia;
                mayorGanancia.clear(); 
                mayorGanancia.add(ciudad.getId());
            } else if (ganancia == mayorGananciaActual && !mayorGanancia.contains(ciudad.getId())) {
                
                mayorGanancia.add(ciudad.getId());
            }
    
            int perdida = ciudad.getPerdidas();
            if (perdida > mayorPerdidaActual) {
                mayorPerdidaActual = perdida;
                mayorPerdida.clear(); 
                mayorPerdida.add(ciudad.getId());
            } else if (perdida == mayorPerdidaActual && !mayorPerdida.contains(ciudad.getId())) {
                mayorPerdida.add(ciudad.getId());
            }
        }
    }
    
    public void actualizarInfoCiudad(Traslado t) {
        int indiceCiudadGana = t.getOrigen(); //O(1)
        int monto = t.getGananciaNeta();//O(1)
        int indiceCiudadPierde = t.getDestino();//O(1)
       // int perdida = t.getGananciaNeta();//O(1)
    
        ciudades[indiceCiudadGana].agregarGanancias(monto); //O(1)
        ciudades[indiceCiudadPierde].agregarPerdidas(monto);//O(1)
    

        cantGanancia += monto;//O(1)
        cantTraslados++;//O(1)
    
        heapSuperavit.modificarEnHeap(ciudades[indiceCiudadGana].getPosHeapSuperavit());//O(logC)
        heapSuperavit.modificarEnHeap(ciudades[indiceCiudadPierde].getPosHeapSuperavit());//O(logC)
        maxSuperavit();
    } 
    

      private void sincronizarHeapAntiguo(int[] indices){
        //en esta funcion le paso los elementos a borrar
        for(int index : indices){
            this.heapAntiguo.eliminarPorPosicion(index);

        }
    }

    private void sincronizarHeapRedituable(int[] indices){
        //en esta funcion le paso los elementos a borrar
        for(int index : indices){
            this.heapRedituable.eliminarPorPosicion(index);
        }
    }

 


    
//En caso de empate, devuelve la que tiene menor identificador
    private void maxSuperavit(){
        
        int idMaxS = this.mayorSuperavit; 
        int actualS = this.ciudades[idMaxS].getSuperavit();
        if( actualS < heapSuperavit.getMax().getSuperavit()){
            this.mayorSuperavit = heapSuperavit.getMax().getId();
        }
        else if(actualS == heapSuperavit.getMax().getSuperavit() 
            &&  heapSuperavit.getMax().getId() < idMaxS ){
                this.mayorSuperavit = heapSuperavit.getMax().getId();
        }
       
    //O(1)
    }
}    
    
        

