package boardgame;
public class Position {
    // Atributos | Atributes.
    private int row;
    private int column;

    // Construtores | Constructors.
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    // Getters e Setters.
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }

    // Método.
    public void setValues(int row, int column){
        this.row = row;
        this.column = column;
    }

    // Sobrescrevendo método toString | Overrider toString method
    @Override
    public String toString() {
        return "Linha - Line: "+row+" | Coluna - Column: "+column;
    }
}