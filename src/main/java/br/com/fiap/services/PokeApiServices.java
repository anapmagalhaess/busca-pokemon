package br.com.fiap.services;

import br.com.fiap.pokemon.Pokemon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokeApiServices {

    public Pokemon getPokemon(String pokemon) throws Exception {

        Pokemon pokemonObjeto = null;

        // api + valor do input
        String url = "https://pokeapi.co/api/v2/pokemon/"
                + pokemon.trim().toLowerCase();

        // informa onde o valor deve ir
        HttpClient client = HttpClient.newHttpClient();

        // GET: busca de info | BUILD: finaliza e "empacota" a info
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // envia e aguarda retorno
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        // verifica se o status retorna OK
        if (response.statusCode() == 200) {

            // guarda a resposta no formato json
            String json = response.body();

            // extrai os dados usando Gson
            JsonObject jsonObject =
                    new Gson().fromJson(json, JsonObject.class);

            String id = String.valueOf(
                    jsonObject.get("id").getAsInt()
            );

            String nome = inicialMaiusc(
                    jsonObject.get("name").getAsString()
            );

            String sprite = null;

            JsonObject spritesObj =
                    jsonObject.getAsJsonObject("sprites");

            // sprite front_default é a imagem padrão frontal
            if (spritesObj != null &&
                    !spritesObj.get("front_default").isJsonNull()) {

                sprite = spritesObj
                        .get("front_default")
                        .getAsString();
            }

            // busca tipos
            String tipos = extracaoTipos(jsonObject);

            // cria o objeto Pokemon
            pokemonObjeto = new Pokemon(
                    id,
                    nome,
                    tipos,
                    sprite
            );
        }

        return pokemonObjeto;
    }

    // extrai e junta os tipos do pokemon
    private static String extracaoTipos(JsonObject jsonObject) {

        StringBuilder tipos = new StringBuilder();

        JsonArray tiposLista =
                jsonObject.getAsJsonArray("types");

        if (tiposLista != null) {

            for (int i = 0; i < tiposLista.size(); i++) {

                JsonObject tipoPosicao =
                        tiposLista.get(i).getAsJsonObject();

                JsonObject tipoInfo =
                        tipoPosicao.getAsJsonObject("type");

                String tipoNome =
                        tipoInfo.get("name").getAsString();

                if (tipos.length() > 0) {
                    tipos.append(", ");
                }

                tipos.append(inicialMaiusc(tipoNome));
            }
        }

        return tipos.length() > 0
                ? tipos.toString()
                : "Desconhecido";
    }

    // metodo que formata a primeira letra em maiúscula
    private static String inicialMaiusc(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase()
                + str.substring(1);
    }
}
