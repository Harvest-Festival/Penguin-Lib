package uk.joshiejack.penguinlib.data.database;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class VeryBasicSQLParser {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<String, Table> database;
    private Action action;

    //Predicates
    public VeryBasicSQLParser(Map<String, Table> database, String query) {
        this.database = database;
        this.action = query.startsWith("delete") ? Action.DELETE : query.startsWith("update") ? Action.UPDATE : Action.ERROR;
        if (this.action == Action.ERROR)
            LOGGER.error("Invalid SQL was loaded, did not give instruction to UPDATE or DELETE");
        query = query.substring(6); //We have the action we now to work out the table(s)


        //trim all white space between clauses
        //DELETE FROM bait WHERE (x='500' AND y='200') OR NOT x='50'

        // (x=2 and (x=1 or y=2) and (x=2 or y=1)) OR y=3

        //BRACKET ( = Start of Statement, Everything up to
        //Statement s0 = new Statement();
        //Statement s1 = new Statement()
        //s1.add('x=2')
        //s1.add('and')
        //Statement s2 = new Statement();
        //s2.add('x=1)'
        //s2.add('or')
        //s2.add('y=2)'
        //s1.add(s2)
        //s1.add(and)
        //Statement s3 = new Statement();
        //s3.add('x=2')
        //s3.add('or)'
        //s3.add('y=1')
        //s1.add(s3)
        //s0.add('s1')
        //s0.add('or')
        //s0.add('y=3')

        // ( = Create a new statement
        // ) = Add the current statement to the previous one and return to the previous one

        // (STATEMENT_1 AND STATEKENT_2 AND STATEMENT_3) OR STATEMENT_4
        //STATEMENT 1/2 OR STATEMENT 3
    }

    public Collection<Table> from (String table) {
        return table.equals("*") ? database.values() : database.containsKey(table) ? Lists.newArrayList(database.get(table)) : new ArrayList<>();
    }

    public enum Action {
        DELETE, UPDATE, ERROR
    }
}
