/**
 * 
 */
package desenvolvimento;

/**
 * @author JN
 *
 */

	import java.util.Arrays;
	import java.util.stream.Collectors;

	public class Enderecos {

	    public static String formatarListaEnderecos(String malformatado) {
	        return Arrays
	                .asList(malformatado.split(";"))
	                .stream()
	                .map(t -> t.replace("<END>", "").replace("</END>", "").trim())
	                .filter(t -> !t.isEmpty())
	                .map(t -> "<END>" + t + "</END>")
	                .collect(Collectors.joining());
	    }

	    public static void main(String[] args) {
	        String texto = "<END>Av. Dr. Walter Belian, nº 2.230, Distrito Industrial, João Pessoa-PB, com CNPJ nº 07.526.557/0013-43e Inscrição Estadual nº 16.218.7157; (NR) II - Sergipe, localizada na Rodovia BR-101, s/nº, km 133, Distrito Industrial, Estância-SE, com CNPJ nº 07.526.577/0012-62 e Inscrição Estadual nº 27.142.202-5; (NR) III - Camaçari, localizada na Rua João Úrsulo, nº 1.620, Polo Petroquímico, Camaçari-BA, com CNPJ nº 07.526.557/0015-05e Inscrição Estadual nº 110.250.399;<END>";
	        String formatado = formatarListaEnderecos(texto);
	        System.out.println(formatado);
	    }
	}
