package com.erikmafo.chess.gui.components;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 12.01.17.
 */
public class ChessboardTest extends ApplicationTest {

    private Chessboard chessboard;

    private EventHandler<PieceDroppedEvent> pieceDroppedEventEventHandler = mock(EventHandler.class);
    private EventHandler<ChessboardEvent> pieceClickedHandleer = mock(EventHandler.class);

    PieceImageView pieceImageView;


    @BeforeClass
    public static void before() {

        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }

    }

    @Override
    public void start(Stage stage) {

        chessboard = new Chessboard();
        chessboard.getStylesheets().addAll("/css/main.css");
        chessboard.setPrefSize(300, 300);
        chessboard.setOnPieceDropped(pieceDroppedEventEventHandler);
        chessboard.setOnPieceClicked(pieceClickedHandleer);

        Scene scene = new Scene(chessboard, 800, 600);
        stage.setScene(scene);
        stage.show();

        pieceImageView = new PieceImageView("white", "pawn");
        pieceImageView.setId("piece");
        chessboard.put("e2", pieceImageView);

        chessboard.setColorToMove("white");

    }


    @Test
    public void shouldFireEventWhenPieceIsDraggedAndDropped() throws Exception {

        drag("#piece", MouseButton.SECONDARY).dropTo("#e4");

        ArgumentCaptor<PieceDroppedEvent> argumentCaptor = ArgumentCaptor.forClass(PieceDroppedEvent.class);

        verify(pieceDroppedEventEventHandler).handle(argumentCaptor.capture());
    }

    @Test
    public void shouldPlacePieceAtDroppedLocationIfDropIsAccepted() throws Exception {

        chessboard.setOnPieceDropped(event -> event.setPieceDropAccepted(true));

        drag("#piece", MouseButton.PRIMARY).dropTo("#e3");

        assertTrue(point("#e3").query().getX() == point("#piece").query().getX());
        assertTrue(point("#e3").query().getY() == point("#piece").query().getY());
    }


    @Test
    public void shouldFireEventWhenSquareIsClicked() throws Exception {

        String square = "d5";

        chessboard.setOnSquareClicked(pieceClickedHandleer);

        clickOn("#" + square);

        ArgumentCaptor<ChessboardEvent> argumentCaptor = ArgumentCaptor.forClass(ChessboardEvent.class);

        verify(pieceClickedHandleer).handle(argumentCaptor.capture());
    }


    @Test
    public void shouldFireEventWhenPieceIsClicked() throws Exception {

        clickOn("#piece");

        verify(pieceClickedHandleer).handle(any(ChessboardEvent.class));

    }
}