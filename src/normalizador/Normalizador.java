package normalizador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import mamografia.Util;


public class Normalizador {
    
    public static void main(String[] args) {
        double[][] banco = new double[961][7];
                
        Util.carregaBanco(banco);
        Formulas.infereDados(banco);
        Formulas.normaliza(banco);
        salvar(banco);
        
        //System.out.println(banco[0][0] + ", " + banco[0][1] + ", " + banco[0][2] + ", " + banco[0][3] + ", " + banco[0][4] + ", " + banco[0][5] + ", " + banco[0][6]);
        //double[][] malignos = new double[30][7];
        //double[][] benignos = new double[30][7];
        //if(Formulas.divideBanco(banco, malignos, benignos))
        //    System.out.println("Deu certo, são " + malignos.length + " malignos e " + benignos.length + " benignos");
    }
    
    private static void salvar(double[][] banco){
        File file = new File("C:\\Users\\victo\\Documents\\NetBeansProjects\\Mamografia\\Dados\\mammographic_masses_inference.data");
        try{
            if(!file.exists())
                file.createNewFile();
            
            FileWriter out = new FileWriter(file);
            
            for(int i=0; i< banco.length; i++){
                for(int j=0; j<banco[i].length; j++){
                    if(j != banco[i].length-1)
                        out.append(String.valueOf(banco[i][j]) + ",");
                    else
                        out.append(String.valueOf(banco[i][j]));
                }
                out.append("\n");
            }

            out.close();
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
