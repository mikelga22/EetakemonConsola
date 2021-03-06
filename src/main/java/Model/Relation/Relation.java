package Model.Relation;

public class Relation extends RelationDAO{
    private int id;
    private int idUser;
    private int idEetakemon;
    private int level;

    public Relation(){}

    public Relation(int idUser, int idEetakemon, int level){
        this.idUser=idUser;
        this.idEetakemon=idEetakemon;
        this.level=level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEetakemon() {
        return idEetakemon;
    }

    public void setIdEetakemon(int idEetakemon) {
        this.idEetakemon = idEetakemon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
