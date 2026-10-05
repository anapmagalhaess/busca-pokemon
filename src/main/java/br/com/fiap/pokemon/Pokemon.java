package br.com.fiap.pokemon;

public class Pokemon {

    //visibilidade, tipo e atributo
    private String id;
    private String nome;
    private String tipos;
    private String sprite;

    //constructor vazio
    public Pokemon() {
    }
    //constructor cheio
    public Pokemon(String id, String nome, String tipos, String sprite) {
        this.id = id;
        this.nome = nome;
        this.tipos = tipos;
        this.sprite = sprite;
    }
    //getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipos() {
        return tipos;
    }

    public void setTipos(String tipos) {
        this.tipos = tipos;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
    // toString
    @Override
    public String toString() {
        return "Pokémon" +
                "\nNúmero: #" + id +
                "\nNome: " + nome +
                "\nTipo(s): " + tipos;
    }
}