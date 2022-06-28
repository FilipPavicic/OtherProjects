package wut.weiti.edcs.ludo;

public enum Codes {
    HELLO
    {
        @Override
        public String toString() {
            return "hello";
        }
    },
    HELLO_RPL{
        @Override
        public String toString() {
            return "hello_rpl";
        }
    },
    TEXT{
        @Override
        public String toString() {
            return "text";
        }
    },
    EXIT{
        @Override
        public String toString() {
            return "exit";
        }
    },
    INVITE{
        @Override
        public String toString() {
            return "invite";
        }
    },
    ACC_INVITE{
        @Override
        public String toString() {
            return "acc_invite";
        }
    },
    START{
        @Override
        public String toString() {
            return "start";
        }
    },
    LEADER{
        @Override
        public String toString() {
            return "leader";
        }
    },
    ON_MOVE{
        @Override
        public String toString() {
            return "on_move";
        }
    },
    PLAYED_MOVE{
        @Override
        public String toString() {
            return "played_move";
        }
    },
    WINNER{
        @Override
        public String toString() {
            return "winner";
        }
    },
    RETURN{
        @Override
        public String toString() {
            return "return";
        }
    },
    UPDATE_STATE{
        @Override
        public String toString() {
            return "update_state";
        }
    },
    TEST{
        @Override
        public String toString() {
            return "test";
        }
    },
}
