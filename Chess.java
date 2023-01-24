import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Chess extends Frame {
    private Timer timer = null; //timer

    private final int sql=150; //size of square
    private final int w=1230, h=1430; //window size
    piece board[][]= new piece [8][8]; //board

    private boolean[][] onClick = new boolean [8][8]; //checks for clicks
    private boolean firstClick = true; //checks if first click
    private boolean Moveable[][] = new boolean[8][8]; //checks if certain piece can move

    private boolean booleanundo = true; //allows undo
    private boolean bqc, bkc, wqc, wkc; //allows cancels

    private String turn = "white"; //turns
    private String[] NumtoAl = {"a", "b", "c", "d", "e", "f", "g", "h"}; //for piece's movement

    private int ci, cj;
    private int MaxTime=300; //max time
    private int Inc=1; //time added for each turn
    private int wtime, btime; //time left for white and black pieces
    private int nmoves; //total number of moves

    private Label lLastMove = null ; //last turn
    private Label lWhiteTimer = null; //time left for white piece
    private Label lBlackTimer = null; //time left for black piece

    private JButton bUndo = null; //undo
    private JButton bForfeit = null; //forfeit match
    private JButton bDraw = null; //draw
    private JButton bStart = null; //restart
    private JButton bSettime = null; //timer settings
    private JButton bSetundo = null; //undo

    private JButton bBoard[][] = new JButton[8][8]; //board

    private ImageIcon icon_light = new ImageIcon("pic/light.png"); //icon for light background
    private ImageIcon icon_dark = new ImageIcon("pic/dark.png"); //icon for dark background

    class BoardState{ //check status of board
        int board[][] = new int[8][8];
        String turn, lastmove;
        boolean bqc, bkc, wqc, wkc;
    }

    BoardState[] bstate = new BoardState[600];

    abstract class piece{ //pieces
        int i, j; //location of pieces
        String color, boardcolor, name; //piece color, color of square, type of piece
        int ind; 
        ImageIcon Icon, clickIcon; //icon, icon when clicked

        void move(int a, int b){ //moves piece to specific location
            this.i=a;
            this.j=b;
            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";

            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
        }

        abstract void setMoveable(); //decides which space to move to
    }

    class Pawn extends piece{ 
        Pawn(int a, int b, String c){ //resets
            this.i=a;
            this.j=b;
            this.color=c;
            this.name="pawn";

            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";
            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
            clickIcon=new ImageIcon("pic/"+this.color+"_"+this.name+"_clicked.png");

            ind=1;
        }

        @Override
        void move(int a, int b){ //override
            this.i=a;
            this.j=b;
            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";

            if(this.i==0 || this.i==7){ //changes to wanted piece if piece reaches end
                Object[] promotion = {"Knight", "Bishop", "Rook", "Queen"};
                Label label = new Label("Promote to another piece: ");
                label.setFont(new Font("Arial", Font.PLAIN, 20));
                String s = (String) JOptionPane.showInputDialog(null, label, "Promotion", JOptionPane.PLAIN_MESSAGE, null, promotion, "Queen");
                promote(this.i, this.j, s, this.color);
            }
            Icon = new ImageIcon("pic/" + this.color + "_" + this.name + "_" + this.boardcolor + ".png");
        }

        void setMoveable(){
            if(this.color=="black"){
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(this.i+1 == i){
                            if(board[i][j]==null){
                                if(j==this.j) Moveable[i][j]=true; //moves pawn one square
                                else{}
                            }
                            else if(board[i][j].color=="white" && (Math.abs(this.j-j)==1)) Moveable[i][j]=true; //pawn moves diagonal to capture opponent
                            else Moveable[i][j]=false;
                        }
                        else Moveable[i][j]=false;
                    }
                }
                if(this.i==1 && board[3][j]==null) Moveable[3][j]=true; //first pawn moves two squares
            }
            else{
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(this.i-1 == i){
                            if(board[i][j]==null){
                                if(j==this.j) Moveable[i][j]=true;
                                else {}
                            }
                            else if(board[i][j].color=="black" && (Math.abs(this.j-j)==1)) Moveable[i][j]=true;
                            else Moveable[i][j]=false;
                        }
                        else Moveable[i][j]=false;
                    }
                    if(this.i==6 && board[4][j]==null) Moveable[4][j]=true;
                }
            }
        }
    }

    class Knight extends piece{ //knight
        Knight(int a, int b, String c){ //resets
            this.i=a;
            this.j=b;
            this.color=c;
            this.name="knight";

            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";
            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
            clickIcon=new ImageIcon("pic/"+this.color+"_"+this.name+"_clicked.png");
            ind=2;
        }

        void setMoveable(){
            if(this.color=="black"){
                for(int i=0; i<8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if ((Math.abs(this.i-i) == 2 && Math.abs(this.j-j) == 1) || (Math.abs(this.i-i) == 1 && Math.abs(this.j-j) == 2)) { //movements for knight
                            if (board[i][j] == null) Moveable[i][j] = true;
                            else if (board[i][j].color == "white") Moveable[i][j] = true;
                            else Moveable[i][j] = false;
                        }
                        else Moveable[i][j] = false;
                    }
                }
            }
            if(this.color=="white"){
                for(int i=0; i<8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if ((Math.abs(this.i-i) == 2 && Math.abs(this.j-j) == 1) || (Math.abs(this.i-i) == 1 && Math.abs(this.j-j) == 2)) {
                            if (board[i][j] == null) Moveable[i][j] = true;
                            else if (board[i][j].color == "black") Moveable[i][j] = true;
                            else Moveable[i][j] = false;
                        }
                        else Moveable[i][j] = false;
                    }
                }
            }
        }
    }

    class Bishop extends piece{ //bishop
        Bishop(int a, int b, String c){ //resets
            this.i=a;
            this.j=b;
            this.color=c;
            this.name="bishop";

            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";
            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
            clickIcon=new ImageIcon("pic/"+this.color+"_"+this.name+"_clicked.png");
            ind=3;
        }

        void setMoveable(){
            for(int i=0; i<8; i++) for(int j=0; j<8; j++) Moveable[i][j]=false;

            if(this.color=="black"){ //diagonal movements of bishop
                int i=this.i+1, j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j++; //right down
                }

                i=this.i+1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j--; //left down
                }

                i=this.i-1; j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j++; //right up
                }

                i=this.i-1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j--; //left up
                }
            }

            if(this.color=="white"){
                int i=this.i+1, j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j++;
                }

                i=this.i+1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j--;
                }

                i=this.i-1; j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j++;
                }

                i=this.i-1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j--;
                }
            }
        }
    }

    class Rook extends piece{ //rook / castle
        Rook(int a, int b, String c){ //resets
            this.i=a;
            this.j=b;
            this.color=c;
            this.name="rook";

            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";
            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
            clickIcon=new ImageIcon("pic/"+this.color+"_"+this.name+"_clicked.png");
            ind=4;
        }

        @Override
        void move(int a, int b){ //override
            if(this.j==0 && this.color=="black") bqc=false; 
            if(this.j==7 && this.color=="black") bkc=false;
            if(this.j==0 && this.color=="white") wqc=false;
            if(this.j==0 && this.color=="white") wkc=false;

            this.i=a;
            this.j=b;
            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";

            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
        }

        void setMoveable(){ //movements of rook
            for(int i=0; i<8; i++) for(int j=0; j<8; j++) Moveable[i][j]=false;
            if(this.color=="black"){
                for(int i=this.i+1; i<8; i++){ //아래쪽 방향
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="white"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(int i=this.i-1; i>=0; i--){ //up
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="white"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(int j=this.j+1; j<8; j++){ //right
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="white"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }

                for(int j=this.j-1; j>=0; j--){ //left
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="white"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }
            }

            if(this.color=="white"){
                for(int i=this.i+1; i<8; i++){
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="black"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(int i=this.i-1; i>=0; i--){
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="black"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(int j=this.j+1; j<8; j++){
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="black"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }

                for(int j=this.j-1; j>=0; j--){
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="black"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }
            }
        }
    }

    class Queen extends piece{ //queen
        Queen(int a, int b, String c){ //resets
            this.i=a;
            this.j=b;
            this.color=c;
            this.name="queen";

            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";
            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
            clickIcon=new ImageIcon("pic/"+this.color+"_"+this.name+"_clicked.png");
            ind=5;
        }

        void setMoveable(){ //movements of queen
            for(int i=0; i<8; i++) for(int j=0; j<8; j++) Moveable[i][j]=false;

            if(this.color=="black"){
                int i=this.i+1, j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j++; //right down
                }

                i=this.i+1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j--; //left down
                }

                i=this.i-1; j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j++; //right up
                }

                i=this.i-1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="black") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j--; //left up
                }

                for(i=this.i+1; i<8; i++){ 
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="white"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(i=this.i-1; i>=0; i--){ 
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="white"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(j=this.j+1; j<8; j++){ 
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="white"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }

                for(j=this.j-1; j>=0; j--){ 
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="white"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }
            }

            if(this.color=="white"){
                int i=this.i+1, j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j++;
                }

                i=this.i+1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i++; j--;
                }

                i=this.i-1; j=this.j+1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j++;
                }

                i=this.i-1; j=this.j-1;
                while(0<=i && i<8 && 0<=j && j<8){
                    if(board[i][j]==null) Moveable[i][j]=true;
                    else if(board[i][j].color=="white") break;
                    else{
                        Moveable[i][j]=true;
                        break;
                    }
                    i--; j--;
                }

                for(i=this.i+1; i<8; i++){
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="black"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(i=this.i-1; i>=0; i--){
                    if(board[i][this.j]==null) Moveable[i][this.j]=true;
                    else if(board[i][this.j].color=="black"){
                        Moveable[i][this.j]=true;
                        break;
                    }
                    else break;
                }

                for(j=this.j+1; j<8; j++){
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="black"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }

                for(j=this.j-1; j>=0; j--){
                    if(board[this.i][j]==null) Moveable[this.i][j]=true;
                    else if(board[this.i][j].color=="black"){
                        Moveable[this.i][j]=true;
                        break;
                    }
                    else break;
                }
            }
        }
    }

    class King extends piece{ //king
        King(int a, int b, String c){ //resets
            this.i=a;
            this.j=b;
            this.color=c;
            this.name="king";

            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";
            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
            clickIcon=new ImageIcon("pic/"+this.color+"_"+this.name+"_clicked.png");
            ind=6;
        }

        @Override
        void move(int a, int b){ //override
            if(this.color=="black") {bqc=false; bkc=false;}
            if(this.color=="white") {wqc=false; wkc=false;}

            this.i=a;
            this.j=b;
            if ((this.i+this.j)%2==0) this.boardcolor = "light";
            else this.boardcolor = "dark";

            Icon=new ImageIcon("pic/"+this.color+"_"+this.name+"_"+this.boardcolor+".png");
        }

        void setMoveable(){ //movements of king
            for(int i=0; i<8; i++) for(int j=0; j<8; j++) Moveable[i][j]=false;
            if(this.color=="black"){
                for(int i=this.i-1; i<=this.i+1; i++){
                    for(int j=this.j-1; j<=this.j+1; j++){
                        if(0<=i && i<8 && 0<=j && j<8){
                            if(board[i][j]==null) Moveable[i][j]=true;
                            else if(board[i][j].color=="white") Moveable[i][j]=true;
                            else continue;
                        }
                    }
                }
                if(bqc && board[0][1]==null && board[0][2]==null && board[0][3]==null) Moveable[0][2]=true;
                if(bkc && board[0][5]==null && board[0][6]==null) Moveable[0][6]=true;
            }
            if(this.color=="white"){
                for(int i=this.i-1; i<=this.i+1; i++){
                    for(int j=this.j-1; j<=this.j+1; j++){
                        if(0<=i && i<8 && 0<=j && j<8){
                            if(board[i][j]==null) Moveable[i][j]=true;
                            else if(board[i][j].color=="black") Moveable[i][j]=true;
                            else continue;
                        }
                    }
                }
                if(wqc && board[7][2]==null && board[7][3]==null && board[7][1]==null) Moveable[7][2]=true;
                if(wkc && board[7][5]==null && board[7][6]==null) Moveable[7][6]=true;
            }
        }
    }

    public static void main(String[] args){
        new Chess(); //start
    }

    Chess(){
        makeGUI(); //GUI
        initGame(); //resets game
    }

    void makeGUI() { //GUI
        setSize(w, h); //size of frame

        Panel controls = new Panel(); //panel for button
        Panel labels = new Panel(); //panel for labels
        add(controls, BorderLayout.SOUTH); 
        add(labels, BorderLayout.NORTH);

        Font font = new Font("Arial", Font.PLAIN, 20); //font

        lLastMove = new Label("Last Move:                  "); 
        lWhiteTimer = new Label("White:                    ");
        lBlackTimer = new Label("Black:                    ");
        lLastMove.setSize(new Dimension(200, 50));
        lWhiteTimer.setSize(new Dimension(200, 50));
        lBlackTimer.setSize(new Dimension(200, 50));
        lLastMove.setFont(font); 
        lWhiteTimer.setFont(font);
        lBlackTimer.setFont(font);

        bUndo = new JButton("Undo"); //undo move
        bForfeit = new JButton("Forfeit"); //forfeit match
        bDraw = new JButton("Draw"); //draw
        bStart = new JButton("Start New Game"); //start
        bSettime = new JButton("Time Settings"); //time
        bSetundo = new JButton("Undo Settings"); //undo
        bUndo.setPreferredSize(new Dimension(150, 50)); //size
        bUndo.setFont(font); //font
        bForfeit.setPreferredSize(new Dimension(150, 50));
        bForfeit.setFont(font);
        bDraw.setPreferredSize(new Dimension(150, 50));
        bDraw.setFont(font);
        bStart.setPreferredSize(new Dimension(300, 50));
        bStart.setFont(font);
        bSettime.setPreferredSize(new Dimension(200, 50));
        bSettime.setFont(font);
        bSetundo.setPreferredSize(new Dimension(200, 50));
        bSetundo.setFont(font);

        labels.add(lLastMove); //adds to panel
        labels.add(lWhiteTimer);
        labels.add(lBlackTimer);
        controls.add(bUndo);
        controls.add(bForfeit);
        controls.add(bDraw);
        controls.add(bStart);
        controls.add(bSettime);
        controls.add(bSetundo);

        Panel board = new Panel(); //panel
        add(board); //adds frame
        board.setLayout(null);


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                bBoard[i][j] = new JButton(); //one square
                bBoard[i][j].setSize(sql, sql); //size of square
                bBoard[i][j].setLocation(j * sql,i* sql+40); //setting of square
                board.add(bBoard[i][j]); //adds to board panel

                bBoard[i][j].addActionListener(new ClickListener(i, j)); 
            }
        }

        setVisible(true); 

        addWindowListener(new MyWindowAdapter()); 

        timer = new Timer(1000, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if(turn=="white") wtime--;
                else btime--;
                updateTime();
            }
        });

        bStart.addActionListener(new ActionListener() { //restart
            @Override
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Restart?");
                label.setFont(font);
                int restart = JOptionPane.showConfirmDialog(null, label); 
                if(restart==0){
                    initGame(); 
                }
            }
        });

        bForfeit.addActionListener(new ActionListener() { //forfeit
            @Override
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("ForFeit?");
                label.setFont(font);
                int forfeit = JOptionPane.showConfirmDialog(null, label); 
                if(forfeit==0) {
                    Label forfeitLabel = null;
                    if (turn == "white") {
                        forfeitLabel.setText("White Forfeits");
                    } else {
                        forfeitLabel.setText("Black Forfeits");
                    }
                    forfeitLabel.setFont(font);
                    JOptionPane.showMessageDialog(null,forfeitLabel, "Game Over !!", JOptionPane.INFORMATION_MESSAGE); //notifies game is over
                    initGame(); //restart
                }
            }
        });

        bUndo.addActionListener(new ActionListener() { //undo
            @Override
            public void actionPerformed(ActionEvent e){
                if(booleanundo) undo(); //undos move
            }
        });

        bDraw.addActionListener(new ActionListener() { //draw
            @Override
            public void actionPerformed(ActionEvent e){
                Label label = new Label("Accept Draw?");
                label.setFont(font);
                int draw = JOptionPane.showConfirmDialog(null, label); //asks for draw
                if(draw==0){
                    label.setText("Draw!!");
                    JOptionPane.showMessageDialog(null, label, "Draw" , JOptionPane.INFORMATION_MESSAGE); //shows draw
                    initGame(); //restarts
                }
                else if(draw==1){
                    label.setText("Draw declined");
                    JOptionPane.showMessageDialog(null, label, "Draw declined" , JOptionPane.INFORMATION_MESSAGE); //not a draw
                }
                else{}
            }
        });

        bSettime.addActionListener(new ActionListener(){ //timer
            @Override
            public void actionPerformed(ActionEvent e){
                Label label = new Label("Maximum time in seconds: "); //max time in secs
                label.setFont(font);
                String s = (String) JOptionPane.showInputDialog(null, label, "Set time", JOptionPane.PLAIN_MESSAGE, null, null, "");
                try{ 
                    MaxTime = Integer.parseInt(s);
                }
                catch(Exception e1){ }
                label.setText("Increment in seconds: "); 
                s=(String) JOptionPane.showInputDialog(null, label, "Set increment", JOptionPane.PLAIN_MESSAGE, null, null, "");
                try{
                    Inc = Integer.parseInt(s);
                }
                catch(Exception e2){ }
            }
        });

        bSetundo.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e){
                Label label = new Label("Allow undo?");
                label.setFont(font);
                int undo = JOptionPane.showConfirmDialog(null, label);
                if(undo==0){
                    label.setText("Undo activated");
                    JOptionPane.showMessageDialog(null, label, "Undo settings" , JOptionPane.INFORMATION_MESSAGE); 
                    booleanundo=true;
                }
                else if(undo==1){
                    label.setText("Undo inactivated");
                    JOptionPane.showMessageDialog(null, label, "Undo settings" , JOptionPane.INFORMATION_MESSAGE); 
                    booleanundo=false;
                }
                else{}
            }
        });
    }

    boolean checkCheck(String s){ //checks for certain color
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j]!=null) if(board[i][j].color==s){
                    board[i][j].setMoveable();
                    for(int k=0; k<8; k++){
                        for(int l=0; l<8; l++){
                            if(Moveable[k][l] && board[k][l]!=null) if(board[k][l].name=="king") return true; //returns true if able to move and king is present
                        }
                    }
                }
            }
        }
        return false; //if not, false
    }

    void updateLastMove(String s){
        lLastMove.setText("Last Move: "+s);
    }

    void updateTime(){ //updates time
        lWhiteTimer.setText("White: "+wtime/60+" min "+wtime%60+" sec"); //time left
        lBlackTimer.setText("Black: "+btime/60+" min "+btime%60+" sec");
        if(wtime==0 || btime==0){ //tells time is over for turn
            Label label;
            if(wtime==0) label = new Label("White loses on time");
            else label = new Label("Black loses on time");
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            JOptionPane.showMessageDialog(null, label, "Game Over!" , JOptionPane.INFORMATION_MESSAGE); //notifies that game is over
            initGame(); //restarts
        }
    }

    void undo(){ //method for undo
        if(nmoves>=1) { 
            bstate[nmoves--] = null; 
            bqc = bstate[nmoves].bqc;
            bkc = bstate[nmoves].bkc;
            wqc = bstate[nmoves].wqc;
            wkc = bstate[nmoves].wkc;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bstate[nmoves].board[i][j] == 0) {
                        board[i][j] = null;
                    } else if (bstate[nmoves].board[i][j] / 10 == 0) {
                        if (bstate[nmoves].board[i][j] % 10 == 1) board[i][j] = new Pawn(i, j, "black");
                        if (bstate[nmoves].board[i][j] % 10 == 2) board[i][j] = new Knight(i, j, "black");
                        if (bstate[nmoves].board[i][j] % 10 == 3) board[i][j] = new Bishop(i, j, "black");
                        if (bstate[nmoves].board[i][j] % 10 == 4) board[i][j] = new Rook(i, j, "black");
                        if (bstate[nmoves].board[i][j] % 10 == 5) board[i][j] = new Queen(i, j, "black");
                        if (bstate[nmoves].board[i][j] % 10 == 6) board[i][j] = new King(i, j, "black");
                    } else if (bstate[nmoves].board[i][j] / 10 == 1) {
                        if (bstate[nmoves].board[i][j] % 10 == 1) board[i][j] = new Pawn(i, j, "white");
                        if (bstate[nmoves].board[i][j] % 10 == 2) board[i][j] = new Knight(i, j, "white");
                        if (bstate[nmoves].board[i][j] % 10 == 3) board[i][j] = new Bishop(i, j, "white");
                        if (bstate[nmoves].board[i][j] % 10 == 4) board[i][j] = new Rook(i, j, "white");
                        if (bstate[nmoves].board[i][j] % 10 == 5) board[i][j] = new Queen(i, j, "white");
                        if (bstate[nmoves].board[i][j] % 10 == 6) board[i][j] = new King(i, j, "white");
                    }
                }
            }
            for (int i = 0; i < 8; i++) { //updates icon
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == null) {
                        if ((i + j) % 2 == 0) bBoard[i][j].setIcon(icon_light);
                        else bBoard[i][j].setIcon(icon_dark);
                    } else bBoard[i][j].setIcon(board[i][j].Icon);
                }
            }
            turn = bstate[nmoves].turn;
            updateLastMove(bstate[nmoves].lastmove);
        }
    }

    void initGame() { //method to start game
        for(int i=0; i<8; i++) for(int j=0; j<8; j++) board[i][j]=null; //resets everything
        for(int i=0; i<8; i++) for(int j=0; j<8; j++) onClick[i][j]=false;
        turn = "white";
        bqc=true;
        bkc=true;
        wqc=true;
        wkc=true;
        updateLastMove("");

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                onClick[i][j]=false;
            }
        }

        wtime=MaxTime;
        btime=MaxTime;
        nmoves=0;
        bstate[0]=new BoardState();
        bstate[0].lastmove="";

        for (int i = 0; i < 8; i++){ 
            board[1][i] = new Pawn(1, i, "black");
            board[6][i] = new Pawn(6, i, "white");
        }
        board[0][0]= new Rook(0, 0, "black"); board[7][0] = new Rook(7, 0, "white");; board[0][7] = new Rook(0, 7, "black");; board[7][7] = new Rook(7, 7, "white");; //rook
        board[0][1]= new Knight(0, 1, "black"); board[7][1] = new Knight(7, 1, "white"); board[0][6] = new Knight(0, 6, "black"); board[7][6] = new Knight(7, 6, "white"); //knight
        board[0][2]= new Bishop(0, 2, "black"); board[7][2] = new Bishop(7, 2, "white"); board[0][5] = new Bishop(0, 5, "black"); board[7][5] = new Bishop(7, 5, "white"); //bishop
        board[0][3]= new Queen(0, 3, "black"); board[0][4]= new King(0, 4, "black"); //black king and queen
        board[7][3]= new Queen(7, 3, "white"); board[7][4]= new King(7, 4, "white"); //white king and queen

        for(int i=0; i<8; i++){ //settings for icon
            for(int j=0; j<8; j++){
                if(board[i][j]==null && (i+j)%2 == 0) bBoard[i][j].setIcon(icon_light);
                else if(board[i][j]==null && (i+j)%2 == 1) bBoard[i][j].setIcon(icon_dark);
                else bBoard[i][j].setIcon(board[i][j].Icon);
            }
        }

        timer.start(); //starts timer
    }

    void promote(int a, int b, String s, String c){ //method for promotion
        if(s=="Queen") board[a][b]=new Queen(a, b, c);
        if(s=="Rook") board[a][b]=new Rook(a, b, c);
        if(s=="Bishop") board[a][b]=new Bishop(a, b, c);
        if(s=="Knight") board[a][b]=new Knight(a, b, c);

        bBoard[a][b].setIcon(board[a][b].Icon);
    }

    class ClickListener implements ActionListener{
        private int i, j;
        ClickListener(int i, int j){ 
            this.i=i;
            this.j=j;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if(onClick[i][j]) { //if same square is clicked twice
                onClick[i][j]=false; //returns to unclicked state
                firstClick=true;
                bBoard[i][j].setIcon(board[i][j].Icon);
            }
            else if(firstClick==true){ //if clicked once
                if(board[i][j]!=null) { //only when a piece is present
                    if(board[i][j].color==turn){ //if designated turn
                        firstClick = false; //next click is shown
                        onClick[i][j] = true; //to be already clicked
                        ci = i; //stores clicked setting
                        cj = j;
                        board[i][j].setMoveable(); //saves possible movements
                        bBoard[i][j].setIcon(board[i][j].clickIcon); //changes icon to clicked
                    }
                }
            }
            else if(Moveable[i][j]==true){ 
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        bstate[nmoves].bqc=bqc; 
                        bstate[nmoves].bkc=bkc;
                        bstate[nmoves].wqc=wqc;
                        bstate[nmoves].wkc=wkc;
                        for(int x=0; x<8; x++){
                            for(int y=0; y<8; y++){
                                if(board[x][y]!=null) {
                                    bstate[nmoves].board[x][y] = board[x][y].ind;
                                    if (board[x][y].color == "white") bstate[nmoves].board[x][y] += 10;
                                }
                                else bstate[nmoves].board[x][y]=0;
                            }
                        }
                        bstate[nmoves].turn=turn;
                        nmoves++; 

                        if(board[ci][cj].name=="king"){ 
                            if(board[ci][cj].color=="black"){
                                if(bqc && i==0 && j==2){
                                    board[0][3]=board[0][0];
                                    board[0][0]=null;
                                    board[0][3].move(0, 3);
                                    bBoard[0][3].setIcon(board[0][3].Icon);
                                    bBoard[0][0].setIcon(icon_light);
                                    bqc=false;
                                }
                                if(bkc && i==0 && j==6){
                                    board[0][5]=board[0][7];
                                    board[0][7]=null;
                                    board[0][5].move(0, 5);
                                    bBoard[0][5].setIcon(board[0][5].Icon);
                                    bBoard[0][7].setIcon(icon_dark);
                                    bkc=false;
                                }
                            }
                            if(board[ci][cj].color=="white"){
                                if(wqc && i==7 && j==2){
                                    board[7][3]=board[7][0];
                                    board[7][0]=null;
                                    board[7][3].move(7, 3);
                                    bBoard[7][3].setIcon(board[7][3].Icon);
                                    bBoard[7][0].setIcon(icon_dark);
                                    wqc=false;
                                }
                                if(wkc && i==7 && j==6){
                                    board[7][5]=board[7][7];
                                    board[7][7]=null;
                                    board[7][5].move(7, 5);
                                    bBoard[7][5].setIcon(board[7][5].Icon);
                                    bBoard[7][7].setIcon(icon_light);
                                    wkc=false;
                                }
                            }
                        }

                        board[i][j]=board[ci][cj]; 
                        board[ci][cj] = null; 
                        board[i][j].move(i, j); 

                        bBoard[i][j].setIcon(board[i][j].Icon); 
                        if((ci+cj)%2 == 0 ) bBoard[ci][cj].setIcon(icon_light);
                        else bBoard[ci][cj].setIcon(icon_dark);
                        firstClick=true; 
                        onClick[ci][cj]=false;
                        onClick[i][j]=false;

                        updateLastMove(NumtoAl[cj]+(8-ci)+" to "+NumtoAl[j]+(8-i)); 

                        if(checkCheck(turn)){ 
                            Label check = new Label("Check!");
                            check.setFont(new Font("Arial", Font.PLAIN, 20));
                            JOptionPane.showMessageDialog(null, check, "Check!!!" , JOptionPane.INFORMATION_MESSAGE);
                        }
                        if(turn=="white"){
                            wtime+=Inc;
                            updateTime();
                            turn="black";
                        }
                        else{
                            btime+=Inc;
                            updateTime();
                            turn="white";
                        }

                        bstate[nmoves]=new BoardState(); 
                        bstate[nmoves].lastmove=NumtoAl[cj]+(8-ci)+" to "+NumtoAl[j]+(8-i); 

                        if(checkCheck(turn)){ 
                            Label illegal = new Label("Illegal Move!");
                            illegal.setFont(new Font("Arial", Font.PLAIN, 20));
                            JOptionPane.showMessageDialog(null, illegal, "Illegal Move!!!" , JOptionPane.INFORMATION_MESSAGE); 
                            undo(); 
                        }
                    }
                });
                thread.start();
            }
        }
    }

    class MyWindowAdapter extends WindowAdapter{
        public void windowClosing(WindowEvent e){
            System.exit(0);
        }
    }
}
