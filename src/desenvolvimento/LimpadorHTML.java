package desenvolvimento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;

public class LimpadorHTML {
	public static void main(String[] args){
		//Criando inst�ncia do HTMLCleaner
		HtmlCleaner cleaner = new HtmlCleaner();
		
		//Obtendo suas propriedades
		CleanerProperties properties = cleaner.getProperties();

		//Altera��es nas propriedades
		properties.setOmitComments(true);//Omite coment�rios
		properties.setOmitDoctypeDeclaration(true); //Omite declara��o doctype
		properties.setOmitXmlDeclaration(true); //Omite declara��o xml
		properties.setCharset("UTF-8");//Codifica��o UTF-8, pois possui o documento usa acento
		
		//Tags-lixo: a serem eliminadas pelo HTMLCleaner
		properties.setPruneTags("title,meta,link,script,style,input,img,li,ul,ol");

		//Dicion�rio de meses, para convers�o do nome para a forma num�rica
		Map<String, String> meses = new Hashtable<>();
		meses.put("janeiro", "01");
		meses.put("fevereiro", "02");
		meses.put("mar�o", "03");
		meses.put("abril", "04");
		meses.put("maio", "05");
		meses.put("junho", "06");
		meses.put("julho", "07");
		meses.put("agosto", "08");
		meses.put("setembro", "09");
		meses.put("outubro", "10");
		meses.put("novembro", "11");
		meses.put("dezembro", "12");
		

		//Define o caminho de localiza��o dos decretos
		//Caso deseje testar, DEVE-SE ALTERAR PARA O LUGAR QUE O DOCUMENTO SE ENCONTRA
		//Para evitar altera��es posteriores no c�digo. Defina tr�s diret�rios.
		String[] caminhosDecretos = new String[3]; 
		caminhosDecretos[0] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2014\\";
		caminhosDecretos[1] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2015\\";
		caminhosDecretos[2] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2016\\";
		
		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhosDecretos[0]);
		pastas[1] = new File(caminhosDecretos[1]);
		pastas[2] = new File(caminhosDecretos[2]);

		//Strings que cont�m (parte do) endere�o de um arquivo 
		String caminhoPasta;
		String caminhoInicio = "tagsReduzidas\\";//Parte final do endere�o que conter� em cada pasta em caminhosDecreto os arquivos limpos 
		String complementoFinal = ".html";
		String caminhoComplemento;
		String caminho; // caminho = caminhoInicio + caminhoComplemento + caminhoFinal

		//Define um caminho de um documento em .txt para que se salve
		//temporariamente o arquivo ap�s a primeira etapa da limpeza HTML
		String caminhoTemporario = "temporario.html";

		//Usado para conter a lista de endere�os dos arquivos dentro de cada pasta
		File[][] arquivos = new File[3][];
		
		//Usado para criar a pasta no qual ser� salvo os decretos ap�s a limpeza
		File p;
		
		//Usado para referenciar o arquivo atual na itera��o
		File arquivoAtual;
		
		//Buffer
		FileReader fr;
		BufferedReader br;
		FileWriter fw;
		BufferedWriter bw;

		//Tag do HTMLClear, usada para referenciar o in�cio do html
		TagNode tagNode;
		
		//Lista de padr�es em regex usados 
		/*
		String padraoRemoverDivVazio = "<div>\\W*</div>";
		String padraoRemoverSpanVazio = "<span>\\W*</span>";
		String padraoRemoverAVazio = "<a\\W+href=[A-Za-z0-9\\./:\"#?!=&;]*>\\W*</a>";
		String padraoRemoverPVazio = "<p>\\W*</p>";
		String padraoRemoverBVazio = "<b>\\W*</b>";
		String padraoRemoverIVazio = "<i>\\W*</i>";
		
		String padraoRemoverTagsRemanescentes = "<\\w*\\s*\\w*>\\s*(<\\w*\\s*\\w*>\\s*</\\w*>)*\\s*</\\w*>";
		String padraoRemoverAlgunsBRs = "<br />\\s*<div>\\s*</div>\\s*<br />";
		
		String padraoRemoverInicio = "\\s*<div>\\s*<br />\\s*<br />\\s*<div>\\s*<div>\\s*<div>";
		*/
		String padraoRemoverDivExibindoTexto = "<div>\\W*Exibindo\\W*<span>Texto\\s+[(A-Za-z�� &;]*</span>\\s*</div>";
		String padraoRemoverDivInforme = "<div>\\W*Informe\\s*(<a .*>aqui</a>)[a-zA-Z ;&\t]*(</div>)";
		String padraoRemoverPClique = "<p>[A-Za-z; \n\t\r\0\\&\\.,]*<br />[A-Za-z; \n\t\r\0\\&\\.,]*<br />\\s*<br />\\s*</p>";
		String padraoRemoverDivLinks = "<div>\\W*(<a\\W+href=[A-Za-z0-9\\./:\"=?!#&;]*>[a-zA-z \\.\t]*</a>\\s*\\|?\\s*)+</div>";
		String padraoRemoverFinalPagina = "<div>\\s*<p>\\s*Este.*";
		String removerTagAbertura = "<(a( href=\"[A-Za-z0-9\\Q:?&.=;/\"\'\\$%\\E \u00A7_#\u002D\u002E\u00A0]+\")?|b|p|u|s|i|sup|em|span|strong|br|br /|form|body|head|div|ins|sub|h(1|2|3|4|5|6))>";
		String removerTagFim = "</(a|b|p|u|s|i|sup|em|span|strong|br|form|body|head|div|ins|sub|h(1|2|3|4|5|6))>";
		String padraoNumeroDecreto = "DECRETO\\s*.*\\s*(\\d{2,3})\\s*\\.\\s*(\\d{3})\\s*.?\\s*(</b>)?\\s*(<b>)?.?\\s+(DE|DIA)(&nbsp;)*\\s+(\\d{1,2})[a-zA-Z&;��]*\\s+DE\\s+([A-Za-z&��;]+)(�)*\\s*DE\\s+(\\d{4})";

		//Compila��o do padr�o e defini��o de um Matcher
		Pattern padraoNumDataDecreto = Pattern.compile(padraoNumeroDecreto);
		Matcher matcher;
				
		//Quanto maior o valor, mais arquivos passam pela limpeza, mas 
		//em compensa��o, arquivos gigantescos demandar�o um tempo MUITO MUITO maior
		//sendo aconselh�vel at�, de prefer�ncia reduzir o arquivo para o tamanho
		//padr�o
		long tamanhoMaximoArquivo = 1000000L;
		
		//Strings auxiliares
		String linha; //Cont�m a linha de um arquivo html
		String decreto; //Cont�m todo o decreto, ent�o decreto � (linha)*
		String mes; 
		String dia;
		
		//Outra(s) var�veis
		boolean jaPassou; //Indica se j� foi dado o match no padr�o do n�mero/data decreto
		double somaArquivosPasta; //Permite saber a m�dia do tamanho dos arquivos (originais) dentro de cada uma das pastas
		double somaArquivosLimpos; //Permite saber a m�dia do tamanho dos arquivos ap�s a limpeza dentro de cada uma das pastas
		int contadorArquivosLimpos; //Conta o n�mero de arquivos que passou pelo processo de limpeza do total dentro de uma pasta
		int contadorArquivosDuplicados; //Conta o n�mero de arquivos que sobrescrevam a escrita de outros arquivos, provavelmente, s�o duplicatas
		
		//Conjunto de SOMENTE arquivos de uma pasta com decretos a serem limpos
		for (int  i = 0; i < pastas.length; i++){
			arquivos[i] = pastas[i].listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});
		}

		//AQUI COME�A A LIMPEZA
		for (int i = 0; i < caminhosDecretos.length; i++){
			caminhoPasta = caminhosDecretos[i] + caminhoInicio;//Constr�i o caminho da pasta destino
			
			//Seta para zero os contadores e o somador
			somaArquivosPasta = 0;
			somaArquivosLimpos = 0;
			contadorArquivosLimpos = 0;
			contadorArquivosDuplicados = 0;
			
			System.out.println("Arquivos maiores ou duplicados da pasta: " + caminhosDecretos);
			p = new File(caminhoPasta);
			if (!p.exists()){ //Se pasta n�o existe, cria
				p.mkdir();
			}
			
			//Para cada arquivo dentro da pasta
			for (int j = 0; j < arquivos[i].length; j++){
				arquivoAtual = arquivos[i][j];
				if (arquivoAtual.length() > tamanhoMaximoArquivo){
					//Mostra o nome e tamanho dos arquivos que s�o maiores que o tamanhoMaximoArquivo
					System.out.print("--GRANDE-- " + arquivoAtual.getName() + " --GRANDE--");
					System.out.println("Tamanho: " + arquivoAtual.length() + " B");
					continue;//Passa para a pr�xima itera��o
				} else {
					somaArquivosPasta = somaArquivosPasta + arquivoAtual.length();
					contadorArquivosLimpos++;
				}
				
				caminhoComplemento = "D_";//Todo arquivo final come�a com essa constante
				jaPassou = false;
				tagNode = null;
				
				try {
					//Faz a limpeza sob os par�metros indicados
					tagNode = cleaner.clean(arquivos[i][j]);

					String nomeAtributo;
					//obt�m todos os elementos
					TagNode tags[] = tagNode.getAllElements(true);
					Map<String,String> conjuntoAtributosNo;
					List<String> conjuntoAtributosNoListado;

					//remove todos atributos de uma tag
					for (int li = 0; li < tags.length; li++){
						conjuntoAtributosNo = tags[li].getAttributes();
						if (conjuntoAtributosNo.size() > 0){
							conjuntoAtributosNoListado = new ArrayList<>(conjuntoAtributosNo.keySet());
							for (int lu = 0; lu < conjuntoAtributosNo.size(); lu++){
								nomeAtributo = conjuntoAtributosNoListado.get(lu);
								if (!nomeAtributo.equals("href")) //mant�m caso seja href, link externo
									tags[li].removeAttribute(nomeAtributo);
							}
						}	
					}

					//escreve o resultado em um arquivo .txt
					new PrettyHtmlSerializer(properties).writeToFile(
							tagNode, caminhoTemporario, "utf-8",true
							);

					//Arquivo de leitura a partir do caminhoTemporario (string)
					fr = new FileReader(caminhoTemporario);
					//Cria-se o buffer para ler do arquivo
					br = new BufferedReader(fr);

					//L�-se a primeira linha
					linha = br.readLine();
					decreto = "";
					
					while(linha != null){
						decreto = decreto + " " + linha;
						matcher = padraoNumDataDecreto.matcher(decreto);
						//Se der match e n�o tiver entrado no if ainda, entre
						if (matcher.find() && jaPassou == false) {
							jaPassou = true;
							mes = matcher.group(8);
							if (mes.contains("&nbsp;"))
								mes = mes.replaceFirst("&nbsp;", "");
							else if (mes.contains("&Ccedil;"))
								mes = mes.replaceFirst("&Ccedil;", "�");
							else if (mes.equals("SETMBRO"))
								mes = "SETEMBRO";
							mes = mes.trim();
							mes = mes.toLowerCase();
							mes = meses.get(mes);
							dia = matcher.group(7);
							
							//Se tamanho da string dia for igual a 1, adicione um zero � frente
							if (dia.length() == 1){
								dia = "0" + dia;
							}
							
							//Contru��o do caminho complemento, que define o nome do arquivo na pasta
							//O padr�o usado � D_NO.DEC_DD-MM-AAAA
							//Onde NO.DEC: N�mero do decreto
							//E DD-MM-AAAA: Data de publica��o
							caminhoComplemento = caminhoComplemento +
									matcher.group(1) + "." + matcher.group(2) + "_"
									+ dia + "-" + mes + "-" + matcher.group(10);
							//System.out.println("\n"+caminhoComplemento);
						}
						linha = br.readLine();
					}
					//Fecha os recursos
					br.close();
					fr.close();

					caminho = caminhoPasta + caminhoComplemento + complementoFinal;
					File a = new File(caminho);
					if (!a.exists()){ //Verifica se existe o arquivo, sen�o
						a.createNewFile(); //Cria o arquivo para escrever
					}else{
						//Caso a pasta esteja vazia, indica que h� um arquivo (original) com a
						//mesma numera��o de outro decreto, talvez arquivo duplicado
						//Se a pasta destino j� est� com arquivos limpos
						//Recomendamos comentar a linha, para evitar jogar muita informa��o no console
						//ATEN��O, O ARQUIVO ESCREVER� DE TODOO JEITO NO CAMINHO POR ELE INDICADO
						System.out.println("!!DUPLICADO!! " + arquivoAtual.getName() + " !!DUPLICADO!!");
						System.out.println("Caminho que os dados foram escritos: "  + caminho + "Arquivo N�: " + contadorArquivosLimpos);
						contadorArquivosDuplicados++;
						continue;
					}

					//criando os fluxos de escrita
					fw = new FileWriter(a);
					bw = new BufferedWriter(fw);

					//String porEnquanto = decreto.split("Independ�ncia do Brasil\\.")[1];
					//porEnquanto = porEnquanto.split("ANEXO")[0];
					//Remo��o das tags residuais
					decreto = decreto.replaceAll(padraoRemoverDivLinks, "");
					decreto = decreto.replaceFirst(padraoRemoverDivInforme, "");
					decreto = decreto.replaceAll(padraoRemoverDivExibindoTexto, "");
					decreto = decreto.replaceFirst(padraoRemoverPClique, "");
					decreto = decreto.replaceFirst(padraoRemoverFinalPagina, "");
					decreto = decreto.replaceAll(removerTagAbertura, "");
					decreto = decreto.replaceAll(removerTagFim, "");
					decreto = decreto.replaceAll("\t", "");
					decreto = decreto.trim();
					
					//Escreve o decreto
					bw.write(decreto);
					
					//Encerra o fluxo de dados
					bw.flush();
					bw.close();
					fw.close();
					
					somaArquivosLimpos = somaArquivosLimpos + a.length();
				}
				catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("\n" + "Vixe, deu um erro inseperado. Contate o suporte.");
				}		
			}
			
			//COMENTE OS PRINTLNs A SEGUIR CASO N�O QUEIRA VER O RESUMO DA LIMPEZA NAS PASTAS
			System.out.println("Para a pasta: " + caminhoPasta);
			System.out.println("N�mero de arquivos na pasta: " + arquivos[i].length);
			System.out.println("N�mero de arquivos que foram limpos: " + contadorArquivosLimpos);
			System.out.println("N�mero de arquivos provavelmente duplicados: " + contadorArquivosDuplicados);
			System.out.println("M�dia do tamanho dos arquivos (originais) que passaram pela limpeza: " + somaArquivosPasta/contadorArquivosLimpos);
			System.out.println("M�dia do tamanho dos arquivos ap�s a limpeza: " + somaArquivosLimpos/contadorArquivosLimpos + "\n");
		}
	}	
}
