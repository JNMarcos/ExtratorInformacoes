package desenvolvimento;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class Extrator {
	public static void main(String[] args){
		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties properties = cleaner.getProperties();

		properties.setOmitComments(true);
		properties.setOmitUnknownTags(true);
		properties.setPruneTags("title,meta,link,script,style");

		String caminhoComputador = "C:\\Users\\JN\\Documents\\DecretosAlepe";
		String caminhoInicio = "arquivoTexto.aspx";
		String complementoFinal = ".html";
		String caminhoComplemento = "";
		
		String caminho;

		for (int i = 2014; i <= 2016; i++){
			for (int j = 0; j < QUE_ALGUMA_COISA_QUANTIDADE_DE_ARQUIVOS_MAS_COMO; j++){
				caminhoComplemento = "";
				if (j < 10 && j > 0){
					caminhoComplemento = "_00" + i;
				} else if (j >= 10 && j < 100) {
					caminhoComplemento = "_0" + i;
				} else {
					caminhoComplemento = "_" + i;
				}
				
				caminho = caminhoComputador + caminhoInicio + caminhoComplemento + complementoFinal;
				TagNode tagNode = cleaner.clean(caminho);
			}
		}
		
				
	}
}
