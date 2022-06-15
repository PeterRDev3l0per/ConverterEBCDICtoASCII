
package appconverter;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class controller_Converter {
    
    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private static final int UPPER_NIBBLE_MASK = 0xF0;
    private static final int LOWER_NIBBLE_MASK = 0x0F;
    
    public String stringToEBCDIC(String palabra){
        try {
//            System.out.print("CODING ORIGINAL: "+Arrays.toString(palabra.getBytes())+"\n");
            // encode to cp1047 represented as Hex
            byte[] EBCDIC_ARRAY = palabra.getBytes("Cp1047");
            String ebcdic_array="";
            for (byte F : EBCDIC_ARRAY) {
                ebcdic_array+= F&0xFF;
            }
//            System.out.print("EBCDIC_ARRAY: "+ebcdic_array+"\n");
            
            char[] hex = encodeHex(EBCDIC_ARRAY); 
            
            //EN HEX
            String outs="";
            System.out.println(Arrays.toString(hex));
            for (char hex_c: hex) {
                outs += hex_c;
            }
            
            //REGRESAR AL STRING ORIGINAL
            String respMsqBytes = new String(decodeHex(hex), "Cp1047");
//            System.out.println("CODING FORMATO HEX: "+respMsqBytes);
            return outs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ALGÚN CARÁCTER INGRESADO NO VÁLIDO PARA CONVERTIR A EBCDIC.","¡ERROR!",JOptionPane.WARNING_MESSAGE);
//            System.out.println("stringToEBCDIC(); -> EXCEPCION [" +e+"]");
            
            return "Error, ingresa los datos correctamente o datos válidos.";
        }
        //H-->48
    }
    public String EbcdicToSTRING(String palabra){
        try {
            if (palabra.matches("^*[0-9A-Fa-f]*")) {
                //REGRESAR AL STRING ORIGINAL
                String outs = new String(decodeHex(palabra.toCharArray()), "Cp1047");
//                System.out.print("HEXA() TRANSLATE: "+outs+"\n");
//                outs = new String(new String(decodeHex(palabra.toCharArray()),Charset.forName("UTF-8")).getBytes("Cp1047"), "UTF-8");
//                System.out.print("UNICODE TRANSLATE: "+outs+"\n");
                return outs;
            } else {
                JOptionPane.showMessageDialog(null, "ALGÚN CARÁCTER INGRESADO NO ES EBCDIC.","¡ERROR!",JOptionPane.WARNING_MESSAGE);
                return "Error, ingresa los datos correctamente o datos válidos.";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ALGÚN CARÁCTER INGRESADO NO ES EBCDIC.","¡ERROR!",JOptionPane.WARNING_MESSAGE);
//            System.out.println(" EbcdicToSTRING(); -> EXCEPCION [" +e+"]");
            return "Error, ingresa los datos correctamente o datos válidos.";
        }
        //H-->48
    }
    public char[] encodeHex(final byte[] buf) {
        final int buflen = buf.length;
        final char[] ch = new char[buflen * 2];
        for(int i = 0, j = 0; i < buf.length; i++, j += 2) {
            final byte b = buf[i];
            final int upper = (b & UPPER_NIBBLE_MASK) >> 4;
            final int lower = b & LOWER_NIBBLE_MASK;
            ch[j] = HEX_DIGITS[upper];
            ch[j + 1] = HEX_DIGITS[lower];
        }
        return ch;
    }
    public static byte[] decodeHex(final char[] data) {
        final int len = data.length;
        if((len & 0x01) != 0) {
            throw new IllegalArgumentException("Illegal HexaDecimal character");
        }
        final byte[] out = new byte[len >> 1];
        for(int i = 0, j = 0; j < len; i++) {
            int f = hexToDigit(data[j], j) << 4;
            j++;
            f |= hexToDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }
    private static int hexToDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if(digit == -1) {
            throw new IllegalArgumentException("Illegal HexaDecimal character '" + ch
                    + "' at index " + index);
        }
        return digit;
    }

}
