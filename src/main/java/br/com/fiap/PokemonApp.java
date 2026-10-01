package br.com.fiap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.swing.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonApp {
    public static void main(String[] args) {

        // pede numero ou nome do pokemon
        String pokemon = JOptionPane.showInputDialog(
                null,
                "Digite o número ou nome do Pokémon",
                "PokeAPI",
                JOptionPane.QUESTION_MESSAGE
        );

        // validaçao de recebimento de dado caso o input vier vazio
        if (pokemon == null || pokemon.trim().isEmpty()) {
            return;
        }

        try {
            // api + valor do input (para encontrar info do pokemon) | tratamento
            String url = "https://pokeapi.co/api/v2/pokemon/" + pokemon.trim().toLowerCase();

            // informa onde o valor deve ir
            HttpClient client = HttpClient.newHttpClient();

            // GET: busca de info | BUILD: finaliza e "empacota" a info
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            // envia e aguarda retorno | RESPONSE: guarda o que a API responder
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // verifica se o status retorna OK (sucesso)
            if (response.statusCode() == 200) {

                // guarda a resposta no formato json
                String json = response.body();

                // extrai id, nome, imagem e tipo usando o gson
                JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

                String id = String.valueOf(jsonObject.get("id").getAsInt());
                String nome = inicialMaiusc(jsonObject.get("name").getAsString());
                String sprite = null;

                JsonObject spritesObj = jsonObject.getAsJsonObject("sprites");
                //sprite front_default eh a imagem padrao frontal do pokemon
                if (spritesObj != null && !spritesObj.get("front_default").isJsonNull()) {
                    sprite = spritesObj.get("front_default").getAsString();
                }

                //busca tipos no metodo de extracaoTipos
                String tipos = extracaoTipos(jsonObject);

                // monta mensagem de exibicao
                String mensagem =   "Número: #" + id + "\n" +
                        "Nome: " + nome + "\n" +
                        "Tipo(s): " + tipos;

                // carrega a imagem do pokemon
                ImageIcon icon = (sprite != null && !sprite.equals("null")) ? new ImageIcon(new URL(sprite)) : null;

                // JO com dados do pokemon e sprite
                JOptionPane.showMessageDialog(null, mensagem, "Pokémon Encontrado :D", JOptionPane.INFORMATION_MESSAGE, icon);
            } else {
                JOptionPane.showMessageDialog(null, "Pokémon não encontrado :(", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Erro de conexão: " + error.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // extrai e junta os tipos do pokemon usando o gson para navegar pela lista do JSON
    private static String extracaoTipos(JsonObject jsonObject) {
        // StringBuilder | acumula de forma mutavel os textos recebidos
        StringBuilder tipos = new StringBuilder();
        //Utiliza a bibliot para extracao da lista correspondente a chave `types`
        JsonArray tiposLista = jsonObject.getAsJsonArray("types");


        //Validacao
        if (tiposLista != null) {
            for (int i = 0; i < tiposLista.size(); i++) {
                //Pega item de posicao i dentro da lista de tipos e converte para obj Json
                JsonObject tipoPosicao = tiposLista.get(i).getAsJsonObject();
                //No obj tipoPosicao buscamos a chave type com o valor do tipo
                JsonObject tipoInfo = tipoPosicao.getAsJsonObject("type");
                //Pegamos esse valor da chave name e convertemos para texto
                String tipoNome = tipoInfo.get("name").getAsString(); //Usei o getAsStrings pois o toString retornaria a estrutura do Gson com as aspas e nao seria formatado no inicialMaiusc,
                                                                        // queremos extrair somente o valor puro do dado

                // formatacao de texto em caso de mais de 1 tipo
                if (tipos.length() > 0) tipos.append(", ");
                // pega o nome enviado pela api e adiciona na lista
                tipos.append(inicialMaiusc(tipoNome));
            }
        }

        // tamanho de tipos > 0 ? true : false
        return tipos.length() > 0 ? tipos.toString() : "Desconhecido";
    }

    // metodo que formata a primeira letra em maiusc
    private static String inicialMaiusc(String str) {
        // validacao em caso de null
        if (str == null || str.isEmpty()) return str;
        // retorna texto com o caracter de posicao (0,1) em maiusculo | pega o restante da palavra e une com a inicial maiuscula (se nao retornaria somente a letra inicial)
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}