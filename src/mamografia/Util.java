
package mamografia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Util {
    
    
    public static void carregaBanco(double[][] banco){
        try {
            FileReader arq = new FileReader("C:\\Users\\victo\\Documents\\NetBeansProjects\\Mamografia\\Dados\\mammographic_masses_inference.data");
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = "a";
            int i=0;
            
            while (linha != null) {
                linha = lerArq.readLine();
                
                if(linha != null){
                    String dados[] = linha.split(",");
                    int completo=1;
                    
                    for(int k=0; k<6; k++){
                        if(dados[k].equals("?")){
                            dados[k] = "-1";
                            completo = 0;
                        }
                        
                        banco[i][k] = Double.valueOf(dados[k]);
                    }
                    
                    if(completo == 1)
                        banco[i][6] = 1;
                    else
                        banco[i][6] = 0;
                    
                }
                
                i++;
            }

            arq.close();
            
        } catch (IOException e) {
          System.err.printf("Erro na abertura do arquivo: %s.\n",
            e.getMessage());
        }
    }
}
