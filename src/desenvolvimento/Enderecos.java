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
	        String texto = "<END>Av. Dr. Walter Belian, n� 2.230, Distrito Industrial, Jo�o Pessoa-PB, com CNPJ n� 07.526.557/0013-43e Inscri��o Estadual n� 16.218.7157; (NR) II - Sergipe, localizada na Rodovia BR-101, s/n�, km 133, Distrito Industrial, Est�ncia-SE, com CNPJ n� 07.526.577/0012-62 e Inscri��o Estadual n� 27.142.202-5; (NR) III - Cama�ari, localizada na Rua Jo�o �rsulo, n� 1.620, Polo Petroqu�mico, Cama�ari-BA, com CNPJ n� 07.526.557/0015-05e Inscri��o Estadual n� 110.250.399;<END>";
	        String formatado = formatarListaEnderecos(texto);
	        System.out.println(formatado);
	    }
	}
