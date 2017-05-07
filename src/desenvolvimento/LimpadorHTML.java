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
		//Criando instância do HTMLCleaner
		HtmlCleaner cleaner = new HtmlCleaner();
		
		//Obtendo suas propriedades
		CleanerProperties properties = cleaner.getProperties();

		//Alterações nas propriedades
		properties.setOmitComments(true);//Omite comentários
		properties.setOmitDoctypeDeclaration(true); //Omite declaração doctype
		properties.setOmitXmlDeclaration(true); //Omite declaração xml
		properties.setCharset("UTF-8");//Codificação UTF-8, pois possui o documento usa acento
		
		//Tags-lixo: a serem eliminadas pelo HTMLCleaner
		properties.setPruneTags("title,meta,link,script,style,input,img,li,ul,ol");

		//Dicionário de meses, para conversão do nome para a forma numérica
		Map<String, String> meses = new Hashtable<>();
		meses.put("janeiro", "01");
		meses.put("fevereiro", "02");
		meses.put("março", "03");
		meses.put("abril", "04");
		meses.put("maio", "05");
		meses.put("junho", "06");
		meses.put("julho", "07");
		meses.put("agosto", "08");
		meses.put("setembro", "09");
		meses.put("outubro", "10");
		meses.put("novembro", "11");
		meses.put("dezembro", "12");
		

		//Define o caminho de localização dos decretos
		//Caso deseje testar, DEVE-SE ALTERAR PARA O LUGAR QUE O DOCUMENTO SE ENCONTRA
		//Para evitar alterações posteriores no código. Defina três diretórios.
		String[] caminhosDecretos = new String[3]; 
		caminhosDecretos[0] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2014\\";
		caminhosDecretos[1] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2015\\";
		caminhosDecretos[2] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2016\\";
		
		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhosDecretos[0]);
		pastas[1] = new File(caminhosDecretos[1]);
		pastas[2] = new File(caminhosDecretos[2]);

		//Strings que contém (parte do) endereço de um arquivo 
		String caminhoPasta;
		String caminhoInicio = "tagsReduzidas\\";//Parte final do endereço que conterá em cada pasta em caminhosDecreto os arquivos limpos 
		String complementoFinal = ".html";
		String caminhoComplemento;
		String caminho; // caminho = caminhoInicio + caminhoComplemento + caminhoFinal

		//Define um caminho de um documento em .txt para que se salve
		//temporariamente o arquivo após a primeira etapa da limpeza HTML
		String caminhoTemporario = "temporario.html";

		//Usado para conter a lista de endereços dos arquivos dentro de cada pasta
		File[][] arquivos = new File[3][];
		
		//Usado para criar a pasta no qual será salvo os decretos após a limpeza
		File p;
		
		//Usado para referenciar o arquivo atual na iteração
		File arquivoAtual;
		
		//Buffer
		FileReader fr;
		BufferedReader br;
		FileWriter fw;
		BufferedWriter bw;

		//Tag do HTMLClear, usada para referenciar o início do html
		TagNode tagNode;
		
		//Lista de padrões em regex usados 
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
		String padraoRemoverDivExibindoTexto = "<div>\\W*Exibindo\\W*<span>Texto\\s+[(A-Za-zíÍ &;]*</span>\\s*</div>";
		String padraoRemoverDivInforme = "<div>\\W*Informe\\s*(<a .*>aqui</a>)[a-zA-Z ;&\t]*(</div>)";
		String padraoRemoverPClique = "<p>[A-Za-z; \n\t\r\0\\&\\.,]*<br />[A-Za-z; \n\t\r\0\\&\\.,]*<br />\\s*<br />\\s*</p>";
		String padraoRemoverDivLinks = "<div>\\W*(<a\\W+href=[A-Za-z0-9\\./:\"=?!#&;]*>[a-zA-z \\.\t]*</a>\\s*\\|?\\s*)+</div>";
		String padraoRemoverFinalPagina = "<div>\\s*<p>\\s*Este.*";
		String removerTagAbertura = "<(a( href=\"[A-Za-z0-9\\Q:?&.=;/\"\'\\$%\\E \u00A7_#\u002D\u002E\u00A0]+\")?|b|p|u|s|i|sup|em|span|strong|br|br /|form|body|head|div|ins|sub|h(1|2|3|4|5|6))>";
		String removerTagFim = "</(a|b|p|u|s|i|sup|em|span|strong|br|form|body|head|div|ins|sub|h(1|2|3|4|5|6))>";
		String padraoNumeroDecreto = "DECRETO\\s*.*\\s*(\\d{2,3})\\s*\\.\\s*(\\d{3})\\s*.?\\s*(</b>)?\\s*(<b>)?.?\\s+(DE|DIA)(&nbsp;)*\\s+(\\d{1,2})[a-zA-Z&;º°]*\\s+DE\\s+([A-Za-z&Çç;]+)( )*\\s*DE\\s+(\\d{4})";

		//Compilação do padrão e definição de um Matcher
		Pattern padraoNumDataDecreto = Pattern.compile(padraoNumeroDecreto);
		Matcher matcher;
				
		//Quanto maior o valor, mais arquivos passam pela limpeza, mas 
		//em compensação, arquivos gigantescos demandarão um tempo MUITO MUITO maior
		//sendo aconselhável até, de preferência reduzir o arquivo para o tamanho
		//padrão
		long tamanhoMaximoArquivo = 1000000L;
		
		//Strings auxiliares
		String linha; //Contém a linha de um arquivo html
		String decreto; //Contém todo o decreto, então decreto é (linha)*
		String mes; 
		String dia;
		
		//Outra(s) varáveis
		boolean jaPassou; //Indica se já foi dado o match no padrão do número/data decreto
		double somaArquivosPasta; //Permite saber a média do tamanho dos arquivos (originais) dentro de cada uma das pastas
		double somaArquivosLimpos; //Permite saber a média do tamanho dos arquivos após a limpeza dentro de cada uma das pastas
		int contadorArquivosLimpos; //Conta o número de arquivos que passou pelo processo de limpeza do total dentro de uma pasta
		int contadorArquivosDuplicados; //Conta o número de arquivos que sobrescrevam a escrita de outros arquivos, provavelmente, são duplicatas
		
		//Conjunto de SOMENTE arquivos de uma pasta com decretos a serem limpos
		for (int  i = 0; i < pastas.length; i++){
			arquivos[i] = pastas[i].listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});
		}

		//AQUI COMEÇA A LIMPEZA
		for (int i = 0; i < caminhosDecretos.length; i++){
			caminhoPasta = caminhosDecretos[i] + caminhoInicio;//Constrói o caminho da pasta destino
			
			//Seta para zero os contadores e o somador
			somaArquivosPasta = 0;
			somaArquivosLimpos = 0;
			contadorArquivosLimpos = 0;
			contadorArquivosDuplicados = 0;
			
			System.out.println("Arquivos maiores ou duplicados da pasta: " + caminhosDecretos);
			p = new File(caminhoPasta);
			if (!p.exists()){ //Se pasta não existe, cria
				p.mkdir();
			}
			
			//Para cada arquivo dentro da pasta
			for (int j = 0; j < arquivos[i].length; j++){
				arquivoAtual = arquivos[i][j];
				if (arquivoAtual.length() > tamanhoMaximoArquivo){
					//Mostra o nome e tamanho dos arquivos que são maiores que o tamanhoMaximoArquivo
					System.out.print("--GRANDE-- " + arquivoAtual.getName() + " --GRANDE--");
					System.out.println("Tamanho: " + arquivoAtual.length() + " B");
					continue;//Passa para a próxima iteração
				} else {
					somaArquivosPasta = somaArquivosPasta + arquivoAtual.length();
					contadorArquivosLimpos++;
				}
				
				caminhoComplemento = "D_";//Todo arquivo final começa com essa constante
				jaPassou = false;
				tagNode = null;
				
				try {
					//Faz a limpeza sob os parâmetros indicados
					tagNode = cleaner.clean(arquivos[i][j]);

					String nomeAtributo;
					//obtém todos os elementos
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
								if (!nomeAtributo.equals("href")) //mantém caso seja href, link externo
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

					//Lê-se a primeira linha
					linha = br.readLine();
					decreto = "";
					
					while(linha != null){
						decreto = decreto + " " + linha;
						matcher = padraoNumDataDecreto.matcher(decreto);
						//Se der match e não tiver entrado no if ainda, entre
						if (matcher.find() && jaPassou == false) {
							jaPassou = true;
							mes = matcher.group(8);
							if (mes.contains("&nbsp;"))
								mes = mes.replaceFirst("&nbsp;", "");
							else if (mes.contains("&Ccedil;"))
								mes = mes.replaceFirst("&Ccedil;", "Ç");
							else if (mes.equals("SETMBRO"))
								mes = "SETEMBRO";
							mes = mes.trim();
							mes = mes.toLowerCase();
							mes = meses.get(mes);
							dia = matcher.group(7);
							
							//Se tamanho da string dia for igual a 1, adicione um zero à frente
							if (dia.length() == 1){
								dia = "0" + dia;
							}
							
							//Contrução do caminho complemento, que define o nome do arquivo na pasta
							//O padrão usado é D_NO.DEC_DD-MM-AAAA
							//Onde NO.DEC: Número do decreto
							//E DD-MM-AAAA: Data de publicação
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
					if (!a.exists()){ //Verifica se existe o arquivo, senão
						a.createNewFile(); //Cria o arquivo para escrever
					}else{
						//Caso a pasta esteja vazia, indica que há um arquivo (original) com a
						//mesma numeração de outro decreto, talvez arquivo duplicado
						//Se a pasta destino já está com arquivos limpos
						//Recomendamos comentar a linha, para evitar jogar muita informação no console
						//ATENÇÃO, O ARQUIVO ESCREVERÁ DE TODOO JEITO NO CAMINHO POR ELE INDICADO
						System.out.println("!!DUPLICADO!! " + arquivoAtual.getName() + " !!DUPLICADO!!");
						System.out.println("Caminho que os dados foram escritos: "  + caminho + "Arquivo N°: " + contadorArquivosLimpos);
						contadorArquivosDuplicados++;
						continue;
					}

					//criando os fluxos de escrita
					fw = new FileWriter(a);
					bw = new BufferedWriter(fw);

					//String porEnquanto = decreto.split("Independência do Brasil\\.")[1];
					//porEnquanto = porEnquanto.split("ANEXO")[0];
					//Remoção das tags residuais
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
			
			//COMENTE OS PRINTLNs A SEGUIR CASO NÃO QUEIRA VER O RESUMO DA LIMPEZA NAS PASTAS
			System.out.println("Para a pasta: " + caminhoPasta);
			System.out.println("Número de arquivos na pasta: " + arquivos[i].length);
			System.out.println("Número de arquivos que foram limpos: " + contadorArquivosLimpos);
			System.out.println("Número de arquivos provavelmente duplicados: " + contadorArquivosDuplicados);
			System.out.println("Média do tamanho dos arquivos (originais) que passaram pela limpeza: " + somaArquivosPasta/contadorArquivosLimpos);
			System.out.println("Média do tamanho dos arquivos após a limpeza: " + somaArquivosLimpos/contadorArquivosLimpos + "\n");
		}
	}	
}
