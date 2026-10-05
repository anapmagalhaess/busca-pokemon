package br.com.fiap.main;

import br.com.fiap.pokemon.Pokemon;
import br.com.fiap.services.PokeApiServices;

import javax.swing.*;
import java.net.URL;

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


        // validacoes de existencia de valores recebidos ou de conexao
        try {

            PokeApiServices pokeApiServices = new PokeApiServices();

            Pokemon pokemonObjeto = pokeApiServices.getPokemon(pokemon);

            if (pokemonObjeto != null) {

                ImageIcon icon = null;

                if (pokemonObjeto.getSprite() != null &&
                        !pokemonObjeto.getSprite().equals("null")) {

                    icon = new ImageIcon(
                            new URL(pokemonObjeto.getSprite())
                    );
                }

                        JOptionPane.showMessageDialog(
                        null,
                        pokemonObjeto,
                        "Pokémon Encontrado :D",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon
                );

            } else {
                        JOptionPane.showMessageDialog(
                        null,
                        "Pokémon não encontrado :(",
                        "ERRO",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception error) {
                    JOptionPane.showMessageDialog(
                    null,
                    "Erro de conexão: " + error.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}