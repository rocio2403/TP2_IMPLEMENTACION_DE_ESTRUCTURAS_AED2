package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCiudad {
    private Ciudad ciudad;

    @BeforeEach
    public void setUp() {
        ciudad = new Ciudad(1, 100, 200, 100); // id=1, pérdidas=100.0, ganancias=200.0, superávit=100.0
    } 

    @Test
    public void testGetters() {
        assertEquals(1, ciudad.getId());
        assertEquals(100, ciudad.getPerdidas());
        assertEquals(200, ciudad.getGanancias());
        assertEquals(100, ciudad.getSuperavit());
    }

    @Test
    public void testAgregarGanancias() {
        ciudad.agregarGanancias(50);
        assertEquals(250, ciudad.getGanancias()); 
        assertEquals(150, ciudad.getSuperavit());  
    }

    @Test
    public void testAgregarPerdidas() {
        ciudad.agregarPerdidas(50);
        assertEquals(150, ciudad.getPerdidas());  
        assertEquals(50, ciudad.getSuperavit());   
    }
    @Test
    public void testMultipleActualizaciones() {
        //Al iniciar, las ganancias son 200 y las perdidas son 100
        assertEquals(200, ciudad.getGanancias());
        assertEquals(100, ciudad.getPerdidas());
        assertEquals(100, ciudad.getSuperavit());
    
        ciudad.agregarGanancias(50);  // 200 + 50 = 250
        ciudad.agregarGanancias(100); // 250 + 100 = 350
    
        ciudad.agregarPerdidas(30);   // 100 + 30 = 130
        ciudad.agregarPerdidas(70);   // 130 + 70 = 200
    
        assertEquals(350, ciudad.getGanancias());  // Las ganancias tendrian que ser 350
        assertEquals(200, ciudad.getPerdidas());    // Las perdidas tendrian ser 200
        assertEquals(150, ciudad.getSuperavit());   // El superavit deberaa ser 150 (350 - 200)
    }
    
    @Test
    public void testToString() {
        String expected = "Ciudad{id=1, perdidas=100, ganancias=200, superavit=100}";
        assertEquals(expected, ciudad.toString());
    }
}