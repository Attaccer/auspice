package top.auspice.locale.message;

import top.auspice.config.compilers.message.pieces.MessagePiece;

public class MessageObject {

    private MessagePiece[] pieces;


    public MessageObject(MessagePiece[] pieces) {
        this.pieces = pieces;
    }

    public MessagePiece[] getPieces() {
        return pieces;
    }



}
