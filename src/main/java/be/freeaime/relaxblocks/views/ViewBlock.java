/**
 * @Author: Aimé
 * @Date:   2022-03-26 17:01:39
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-26 21:54:42
 */
package be.freeaime.relaxblocks.views;

import be.freeaime.relaxblocks.interfaces.ClickCallBack;
import be.freeaime.relaxblocks.models.Coordinate;

import javafx.scene.control.Button;

public class ViewBlock  extends Button{
    private Coordinate coordinate;
    private final ClickCallBack clickCallBack;

    public ViewBlock(int row, int col, ClickCallBack clickCallBack){
        this.coordinate=new Coordinate(row, col);
        this.clickCallBack=clickCallBack;
        setPrefSize(32, 32);
        this.setOnAction(event->{
            this.clickCallBack.clickUpdate(coordinate.getRow(), coordinate.getCol());
        });
    }
    public ViewBlock(Coordinate coordinate, ClickCallBack clickCallBack){
        this.coordinate= new Coordinate(coordinate.getRow(),coordinate.getCol());
        this.clickCallBack=clickCallBack;
        setPrefSize(32, 32);
        this.setOnAction(event->{
            this.clickCallBack.clickUpdate(this.coordinate.getRow(), this.coordinate.getCol());
        });
    }
    public void updateType(int type) {
        switch (type) {
            case 0:
                this.setDisable(true);
                getStyleClass().clear();
                getStyleClass().addAll("block-empty");
                break;
            case 1:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button","block-o");
                break;
            case 2:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button","block-plus");
                break;
            case 3:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-x");
                break; 
        }
        
    }
    
}
