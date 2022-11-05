package Lexico;

public class Token {
	
	public static final String TK_IDENTIFICADOR = "IDENTIFICADOR";
	public static final String TK_NUMERO = "NUMERO";
	public static final String TK_OPERADOR = "OPERADOR";
	public static final String TK_PONTUACAO = "PONTUACAO";
	public static final String TK_PALAVRA_RESERVADA = "PALAVRA_RESERVADA";
	public static final String TK_OPERADOR_RELACIONAL = "OPERADOR_RELACIONAL";
	public static final String TK_ATRIBUICAO= "ATRIBUICAO";
	public static final String TK_AP = "AP";
	public static final String TK_FP = "FP";
	public static final String TK_ERRO = "ERRO";
	

	private String tipo;
	private String valor;
	
	public Token(String tipo, String valor) {
		this.tipo = tipo;
		this.valor = valor;
	}
	
	public Token() {
		
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "<" + tipo + ", \"" + valor + "\">";
	}
	
	
	
}
