import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Random;
import java.util.Scanner;

class Pair{
    String key;
    int value;

    Pair(String key, int value){
        this.key = key;
        this.value = value;
    }

    Pair(){
        this.key = "";
        this.value = -1;
    }
}

class boolProb{
    int prob;
    boolean isDone;

    boolProb(){
        prob = 0;
        isDone = false;
    }

    boolProb(int prob, boolean isDone){
        this.prob = prob;
        this.isDone = isDone;
    }
}


public class LinkedList {
    private Node head;
    public int size;

    LinkedList(){

        head = new Node();
        size = 0;
    }

    public Node insert(Pair pair){
        //System.out.println("Hi");
        Node tmp = head;
        while(tmp.next() != null){
            tmp = tmp.next();
        }
        Node node = new Node(pair);
        tmp.setNext(node);
        size++;
        return node;
    }

    public Node search(Pair pair){
        Node tmp = head.next();
        while(tmp != null){
            if(pair.key.equals(tmp.pair.key)){
                return tmp;
            }
            tmp = tmp.next();
        }
        return null;
    }

    public Node delete(Node node){
        Node tmp = head.next();
        while(tmp != null){
            if(tmp == node){
                Node tmp1 = head;
                while(tmp1.next() != tmp){
                    tmp1 = tmp1.next();
                }
                tmp1.setNext(tmp.next());
                size--;
                return tmp;
            }
            tmp = tmp.next();
        }
        return null;
    }

    public static int h1(String s, int n) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            ans = (ans << 5) | (ans >>> 27);
            ans = (ans + (int)s.charAt(i)) % n;
        }
        return ans;
    }

    public static void separateChainingFunc(double[][] ansTableSeparateChaining, int n){
        System.out.println("Separate chaining");
        for(double lf = 0.4; lf <= 0.91; lf += 0.1000001){
            int m = (int)(lf * n);
            //System.out.println(m);
            Pair[] keyValuePair = new Pair[m + 1];
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            int length = 7;
            Random rnd = new Random();
            for(int i = 1; i <= m; i++){
                StringBuilder s = new StringBuilder();
                boolean isPresent = false;
                for(int j = 0; j < length; j++){
                    int ind = rnd.nextInt(alphabet.length());
                    char ch = alphabet.charAt(ind);
                    s.append(ch);
                }
                //System.out.println(s);

                String str = s.toString();
                for(int j = 1; j < i; j++){
                    if(keyValuePair[j].key.equals(str)){
                        isPresent = true;
                        break;
                    }
                }
                if(isPresent){
                    i--;
                }
                else{
                    keyValuePair[i] = new Pair(str, i);
                    //System.out.println(keyValuePair[i].key + " " + keyValuePair[i].value);
                }
            }
            System.out.println("For: " + lf);
            int[] hashIndex = new int[m + 1];
            for(int i = 1; i <= m; i++){
                hashIndex[i] = h1(keyValuePair[i].key, n);
                //System.out.println(hashIndex[i]);
            }
            Node[] nodeReference = new Node[m + 1];
            LinkedList[] linkedList = new LinkedList[n + 1];
            for(int i = 0; i < n; i++){
                linkedList[i] = new LinkedList();
            }
            for(int i = 1; i <= m; i++){
                nodeReference[i] = linkedList[hashIndex[i]].insert(keyValuePair[i]);
            }
            int l = (int)(0.1 * m);
            double searchTimeBeforeDeletion = 0.0;
            for(int i = 1; i <= l; i++){
                int searchPair = rnd.nextInt(m) + 1;
                double startTime = System.nanoTime();
                Node node = linkedList[hashIndex[searchPair]].search(keyValuePair[searchPair]);
                double endTime = System.nanoTime();
                //System.out.println(startTime + " " + endTime);
                searchTimeBeforeDeletion += (endTime - startTime);
                if(node == null){
                    System.out.println("Not Found");
                }
            }
            //System.out.println("I am: " + (lf - 0.4) * 10);
            System.out.println("Average time for search before deletion: " + searchTimeBeforeDeletion / l);
            ansTableSeparateChaining[(int)((lf - 0.40000000000) * 10)][0] = (searchTimeBeforeDeletion) / l;
            ansTableSeparateChaining[(int)((lf - 0.40000000000) * 10)][1] = -1;
            Node[] deletedNodes = new Node[m + 1];
            int[] deletedIndex = new int[m + 1];
            boolean[] isDeleted = new boolean[m + 1];
            for(int i = 1; i<= m; i++){
                isDeleted[i] = false;
            }
            for(int i = 1; i <= l; i++){
                int deletePair = rnd.nextInt(m) + 1;
                Node node = nodeReference[deletePair];
                if(isDeleted[deletePair]){
                    i--;
                    //System.out.println("Same paisi mama");
                    continue;
                }
                deletedNodes[i] = node;
                deletedIndex[i] = deletePair;
                isDeleted[deletePair] = true;
                if(linkedList[hashIndex[deletePair]].delete(node) == null){
                    System.out.println("Cannot delete");
                }
            }

            double searchTimeAfterDeletion = 0.0;
            for(int i = 1; i <= l/2; i++){
                double startTime = System.nanoTime();
                Node node = linkedList[deletedIndex[i]].search(deletedNodes[i].pair);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                if(node == null){
                    //System.out.println("Not Found");
                }
            }
            for(int i = l/2 + 1; i <= l; i++){
                int searchPair = rnd.nextInt(m) + 1;
                if(isDeleted[searchPair]){
                    i--;
                    //System.out.println("Koibar delete korbi vai");
                    continue;
                }
                double startTime = System.currentTimeMillis();
                Node node = linkedList[hashIndex[searchPair]].search(keyValuePair[searchPair]);
                double endTime = System.currentTimeMillis();
                searchTimeAfterDeletion += (endTime - startTime);
                if(node == null){
                    System.out.println("Not Found");
                }
            }
            System.out.println("Average time for search after deletion: " + searchTimeAfterDeletion / l);
            ansTableSeparateChaining[(int)((lf - 0.40000000000) * 10)][2] = (searchTimeAfterDeletion) / l;
            ansTableSeparateChaining[(int)((lf - 0.40000000000) * 10)][3] = -1;
        }
        //System.out.println(ansTableSeparateChaining[0][0] + " " + ansTableSeparateChaining[0][2]);
    }

    public static int uniqueHash(int n) {
        int cnt = 0;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int length = 7;
        boolean[] b = new boolean[n];
        for (int i = 0; i < n; i++) {
            b[i] = false;
        }
        String[] strArr = new String[100];
        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            StringBuilder s = new StringBuilder();
            boolean isPresent = false;
            for (int j = 0; j < length; j++) {
                int ind = rnd.nextInt(alphabet.length());
                char ch = alphabet.charAt(ind);
                s.append(ch);
            }
            //System.out.println(s);

            String str = s.toString();
            for(int j = 0; j < i; j++){
                if(strArr[j].equals(str)){
                    isPresent = true;
                    break;
                }
            }
            if(isPresent){
                i--;
            }
            else{
                strArr[i] = str;
                int hc = h1(str, n);
                //System.out.println(hc);
                if(!b[hc]){
                    cnt++;
                    b[hc] =true;
                }
            }
        }
        return cnt;
    }


    public static int linearProbingHashFunc(String key, int j, int n){
        return (h1(key, n) + j) % n;
    }

    public static boolProb insertLinearProbing(Pair[] hashTable, Pair pair, int n){
        //int prob = 0;
        int j = 0;
        int ind = h1(pair.key, n);
        boolean isInserted = false;
        while(j < n){
            if(hashTable[ind].value == -1){
                hashTable[ind].key = pair.key;
                hashTable[ind].value = pair.value;
                isInserted = true;
                //System.out.println("Insert done!");
                break;
                //return prob;
            }
            j++;
            ind = linearProbingHashFunc(pair.key, j, n);
            //prob++;
        }
        return new boolProb(j, isInserted);
    }

    public static boolProb searchLinearProbing(Pair[] hashTable, Pair pair, int n){
        int j = 0;
        int ind = h1(pair.key, n);
        boolean isFound = false;
        while(j < n){
            if(hashTable[ind].value == pair.value && hashTable[ind].key.equals(pair.key)){
                isFound = true;
                //System.out.println("Key found");
                break;
                //return prob;
            }
            else if(hashTable[ind].value == -1 && !(hashTable[ind].key.equals("DELETEDNODE"))){
                //System.out.println("Key not found");
                break;
            }
            j++;
            ind = linearProbingHashFunc(pair.key, j, n);
            //prob++;
        }
        if(!isFound){
            //System.out.println("Key not found" + "-> prob: " + j);
        }
        return new boolProb(j, isFound);
    }

    public static boolProb deleteLinearProbing(Pair[] hashTable, Pair pair, int n){
        int j = 0;
        int ind = h1(pair.key, n);
        boolean isDeleted = false;
        while(j < n){
            if(hashTable[ind].value == pair.value && hashTable[ind].key.equals(pair.key)){
                isDeleted = true;
                hashTable[ind].key = "DELETEDNODE";
                hashTable[ind].value = -1;
                //System.out.println("Key Deleted");
                break;
                //return prob;
            }
            else if(hashTable[ind].value == -1 && !hashTable[ind].key.equals("DELETEDNODE")){
                System.out.println("Key not deleted because not found");
                break;
            }
            j++;
            ind = linearProbingHashFunc(pair.key, j, n);
            //prob++;
        }
        return new boolProb(j, isDeleted);
    }

    public static void linearProbingFunc(double[][] ansTableLinearProbing, int n){
        System.out.println("Linear probing");
        for(double lf = 0.4; lf <= 0.91; lf += 0.1000001){
            int m = (int)(lf * n);
            //System.out.println(m);
            Pair[] keyValuePair = new Pair[m + 1];
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            int length = 7;
            Random rnd = new Random();
            for(int i = 1; i <= m; i++){
                StringBuilder s = new StringBuilder();
                boolean isPresent = false;
                for(int j = 0; j < length; j++){
                    int ind = rnd.nextInt(alphabet.length());
                    char ch = alphabet.charAt(ind);
                    s.append(ch);
                }
                //System.out.println(s);

                String str = s.toString();
                for(int j = 1; j < i; j++){
                    if(keyValuePair[j].key.equals(str)){
                        isPresent = true;
                        break;
                    }
                }
                if(isPresent){
                    i--;
                }
                else{
                    keyValuePair[i] = new Pair(str, i);
                    //System.out.println(keyValuePair[i].key + " " + keyValuePair[i].value);
                }
            }
            System.out.println("For: " + lf);
            int[] hashIndex = new int[m + 1];
            for(int i = 1; i <= m; i++){
                hashIndex[i] = h1(keyValuePair[i].key, n);
                //System.out.println(hashIndex[i]);
            }
            Pair[] hashTable = new Pair[n];
            for(int i = 0; i < n; i++){

                hashTable[i] = new Pair();
            }
            boolean[] isInsertionDone = new boolean[m + 1];
            for(int i = 1; i <= m; i++){
                isInsertionDone[i] = false;
            }
            //long totalProbInsert = 0, totalProbDelete = 0;
            int totalProbSearch1 = 0, totalProbSearch2 = 0, cntInsert = 0;
            for(int i = 1; i <= m; i++){
                boolProb res = insertLinearProbing(hashTable, keyValuePair[i], n);
                //totalProbInsert += res.prob;
                if(res.isDone){
                    cntInsert++;
                    isInsertionDone[i] = true;
                }
            }
            //System.out.println("Total probInsert: " + totalProbInsert);
            double searchTimeBeforeDeletion = 0.0, searchTimeAfterDeletion = 0.0;
            int l = (int)(0.1 * cntInsert);
            for(int i = 1; i <= l; i++){
                int searchPair = rnd.nextInt(m) + 1;
                if(!isInsertionDone[searchPair]){
                    //System.out.println("Mama eta to insert e hoini");
                    i--;
                }
                else {
                    double startTime = System.nanoTime();
                    boolProb res = searchLinearProbing(hashTable, keyValuePair[searchPair], n);
                    double endTime = System.nanoTime();
                    searchTimeBeforeDeletion += (endTime - startTime);
                    totalProbSearch1 += res.prob;
                }
            }
            System.out.println("Average searchProb1: " + (double)(totalProbSearch1 * 1.0 / l));
            System.out.println("Average search time before deletion: " + searchTimeBeforeDeletion / l);
            ansTableLinearProbing[(int)((lf - 0.40000000000) * 10)][0] = (searchTimeBeforeDeletion) / l;
            ansTableLinearProbing[(int)((lf - 0.40000000000) * 10)][1] = (double)(totalProbSearch1 * 1.0 / l);
            //Node[] deletedNodes = new Node[m + 1];
            int[] deletedIndex = new int[m + 1];
            boolean[] isDeleted = new boolean[m + 1];
            for(int i = 1; i<= m; i++){
                isDeleted[i] = false;
            }
            for(int i = 1; i <= l; i++){
                int deletePair = rnd.nextInt(m) + 1;
                if(isDeleted[deletePair]){
                    i--;
                    //System.out.println("Same paisi mama");
                    continue;
                }
                boolProb res = deleteLinearProbing(hashTable, keyValuePair[deletePair], n);
                //totalProbDelete += res.prob;
                if(res.isDone) {
                    isDeleted[deletePair] = true;
                    deletedIndex[i] = deletePair;
                }
            }

            //System.out.println("Total probDelete : " + totalProbDelete);
            for(int i = 1; i <= l/2; i++){
                double startTime = System.nanoTime();
                boolProb res = searchLinearProbing(hashTable, keyValuePair[deletedIndex[i]], n);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                totalProbSearch2 += res.prob;
            }
            //System.out.println("SearchProb2 after only searching deleted items: " + totalProbSearch2);
            for(int i = l/2 + 1; i <= l; i++){
                int searchPair = rnd.nextInt(m) + 1;
                if(isDeleted[searchPair] || !isInsertionDone[searchPair]){
                    i--;
                    //System.out.println("Koibar delete korbi vai");
                    continue;
                }
                double startTime = System.nanoTime();
                boolProb res = searchLinearProbing(hashTable, keyValuePair[searchPair], n);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                totalProbSearch2 += res.prob;
            }
            System.out.println("Average searchProb2: " + (double)(totalProbSearch2 * 1.0 / l));
            System.out.println("Average search time after deletion: " + searchTimeAfterDeletion / l);
            ansTableLinearProbing[(int)((lf - 0.40000000000) * 10)][2] = (searchTimeAfterDeletion) / l;
            ansTableLinearProbing[(int)((lf - 0.40000000000) * 10)][3] = (double)(totalProbSearch2 * 1.0 / l);
        }
    }


    public static int quadraticProbingHashFunc(String key, int j, int n){
        return (h1(key, n) + j + j * j) % n;
    }

    public static boolProb insertQuadraticProbing(Pair[] hashTable, Pair pair, int n){
        //int prob = 0;
        int j = 0;
        int ind = h1(pair.key, n);
        boolean isInserted = false;
        while(j < n){
            if(hashTable[ind].value == -1){
                hashTable[ind].key = pair.key;
                hashTable[ind].value = pair.value;
                isInserted = true;
                //System.out.println("Insert done!");
                break;
                //return prob;
            }
            j++;
            ind = quadraticProbingHashFunc(pair.key, j, n);
            //prob++;
        }
        if(!isInserted){
            System.out.println("Insert not done");
        }
        return new boolProb(j, isInserted);
    }

    public static boolProb searchQuadraticProbing(Pair[] hashTable, Pair pair, int n){
        int j = 0;
        int ind = h1(pair.key, n);
        boolean isFound = false;
        while(j < n){
            if(hashTable[ind].value == pair.value && hashTable[ind].key.equals(pair.key)){
                isFound = true;
                //System.out.println("Key found");
                break;
                //return prob;
            }
            else if(hashTable[ind].value == -1 && !(hashTable[ind].key.equals("DELETEDNODE"))){
                //System.out.println("Key not found");
                break;
            }
            j++;
            ind = quadraticProbingHashFunc(pair.key, j, n);
            //prob++;
        }
        if(!isFound){
            //System.out.println("Key not found" + "-> prob: " + j);
        }
        return new boolProb(j, isFound);
    }

    public static boolProb deleteQuadraticProbing(Pair[] hashTable, Pair pair, int n){
        int j = 0;
        int ind = h1(pair.key, n);
        boolean isDeleted = false;
        while(j < n){
            if(hashTable[ind].value == pair.value && hashTable[ind].key.equals(pair.key)){
                isDeleted = true;
                hashTable[ind].key = "DELETEDNODE";
                hashTable[ind].value = -1;
                //System.out.println("Key Deleted");
                break;
                //return prob;
            }
            else if(hashTable[ind].value == -1 && !hashTable[ind].key.equals("DELETEDNODE")){
                System.out.println("Key not deleted because not found");
                break;
            }
            j++;
            ind = quadraticProbingHashFunc(pair.key, j, n);
            //prob++;
        }
        return new boolProb(j, isDeleted);
    }

    public static void quadraticProbingFunc(double[][] ansTableQuadraticProbing, int n){
        System.out.println("Quadratic probing");
        for(double lf = 0.4; lf <= 0.91; lf += 0.1000001) {
            int m = (int) (lf * n);
            //System.out.println(m);
            Pair[] keyValuePair = new Pair[m + 1];
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            int length = 7;
            Random rnd = new Random();
            for (int i = 1; i <= m; i++) {
                StringBuilder s = new StringBuilder();
                boolean isPresent = false;
                for (int j = 0; j < length; j++) {
                    int ind = rnd.nextInt(alphabet.length());
                    char ch = alphabet.charAt(ind);
                    s.append(ch);
                }
                //System.out.println(s);

                String str = s.toString();
                for (int j = 1; j < i; j++) {
                    if (keyValuePair[j].key.equals(str)) {
                        isPresent = true;
                        break;
                    }
                }
                if (isPresent) {
                    i--;
                } else {
                    keyValuePair[i] = new Pair(str, i);
                    //System.out.println(keyValuePair[i].key + " " + keyValuePair[i].value);
                }
            }
            System.out.println("For: " + lf);
            int[] hashIndex = new int[m + 1];
            for (int i = 1; i <= m; i++) {
                hashIndex[i] = h1(keyValuePair[i].key, n);
                //System.out.println(hashIndex[i]);
            }
            Pair[] hashTable = new Pair[n];
            for (int i = 0; i < n; i++) {

                hashTable[i] = new Pair();
            }
            boolean[] isInsertionDone = new boolean[m + 1];
            for (int i = 1; i <= m; i++) {
                isInsertionDone[i] = false;
            }
            //long totalProbInsert = 0, totalProbDelete = 0;
            int totalProbSearch1 = 0, totalProbSearch2 = 0, cntInsert = 0;
            for (int i = 1; i <= m; i++) {
                boolProb res = insertQuadraticProbing(hashTable, keyValuePair[i], n);
                //totalProbInsert += res.prob;
                if (res.isDone) {
                    cntInsert++;
                    isInsertionDone[i] = true;
                }
            }
            //System.out.println("Total probInsert: " + totalProbInsert);

            double searchTimeBeforeDeletion = 0.0, searchTimeAfterDeletion = 0.0;
            int l = (int) (0.1 * cntInsert);
            for (int i = 1; i <= l; i++) {
                int searchPair = rnd.nextInt(m) + 1;
                if (!isInsertionDone[searchPair]) {
                    //System.out.println("Mama eta to insert e hoini");
                    i--;
                } else {
                    double startTime = System.nanoTime();
                    boolProb res = searchQuadraticProbing(hashTable, keyValuePair[searchPair], n);
                    double endTime = System.nanoTime();
                    searchTimeBeforeDeletion += (endTime - startTime);
                    totalProbSearch1 += res.prob;
                }
            }
            System.out.println("Average searchProb1: " + (double)(totalProbSearch1 * 1.0 / l));
            System.out.println("Average search time before deletion: " + searchTimeBeforeDeletion / l);
            ansTableQuadraticProbing[(int)((lf - 0.4) * 10)][0] = (searchTimeBeforeDeletion) / l;
            ansTableQuadraticProbing[(int)((lf - 0.4) * 10)][1] = (double)(totalProbSearch1 * 1.0 / l);
            //Node[] deletedNodes = new Node[m + 1];
            int[] deletedIndex = new int[m + 1];
            boolean[] isDeleted = new boolean[m + 1];
            for (int i = 1; i <= m; i++) {
                isDeleted[i] = false;
            }
            for (int i = 1; i <= l; i++) {
                int deletePair = rnd.nextInt(m) + 1;
                if (isDeleted[deletePair]) {
                    i--;
                    //System.out.println("Same paisi mama");
                    continue;
                }
                boolProb res = deleteQuadraticProbing(hashTable, keyValuePair[deletePair], n);
                //totalProbDelete += res.prob;
                if (res.isDone) {
                    isDeleted[deletePair] = true;
                    deletedIndex[i] = deletePair;
                }
            }

            //System.out.println("Total probDelete : " + totalProbDelete);
            for (int i = 1; i <= l / 2; i++) {
                double startTime = System.nanoTime();
                boolProb res = searchQuadraticProbing(hashTable, keyValuePair[deletedIndex[i]], n);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                totalProbSearch2 += res.prob;
            }
            //System.out.println("SearchProb2 after only searching deleted items: " + totalProbSearch2);
            for (int i = l / 2 + 1; i <= l; i++) {
                int searchPair = rnd.nextInt(m) + 1;
                if (isDeleted[searchPair] || !isInsertionDone[searchPair]) {
                    i--;
                    //System.out.println("Koibar delete korbi vai");
                    continue;
                }
                double startTime = System.nanoTime();
                boolProb res = searchQuadraticProbing(hashTable, keyValuePair[searchPair], n);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                totalProbSearch2 += res.prob;
            }
            System.out.println("Average searchProb2: " + (double)(totalProbSearch2 * 1.0 / l));
            System.out.println("Average search time after deletion: " + searchTimeAfterDeletion / l);
            ansTableQuadraticProbing[(int)((lf - 0.4) * 10)][2] = (searchTimeAfterDeletion) / l;
            ansTableQuadraticProbing[(int)((lf - 0.4) * 10)][3] = (double)(totalProbSearch2 * 1.0 / l);
        }
    }

    public static int h2(String key, int n, int z){
        int ans = 0;
        for(int i2 = key.length() - 1; i2 >= 0;i2--)
            ans = (z * ans + key.charAt(i2)) % n;
        return ans;
    }

    public static int doubleHashFunc(String key, int j, int n, int z){
        return (h1(key, n) + j * h2(key, n, z)) % n;
    }

    public static boolProb insertDoubleHash(Pair[] hashTable, Pair pair, int n){
        //int prob = 0;
        int j = 0;
        int ind = h1(pair.key, n);
        int startIndex = ind;
        boolean isInserted = false;
        while(j < n){
            if(hashTable[ind].value == -1){
                hashTable[ind].key = pair.key;
                hashTable[ind].value = pair.value;
                isInserted = true;
                //System.out.println("Insert done!");
                break;
                //return prob;
            }
            j++;
            ind = doubleHashFunc(pair.key, j, n, 41);
            //prob++;
            if(ind == startIndex){
                break;
            }
        }
        if(!isInserted){
            System.out.println("Insert not done");
        }
        return new boolProb(j, isInserted);
    }

    public static boolProb searchDoubleHash(Pair[] hashTable, Pair pair, int n){
        int j = 0;
        int ind = h1(pair.key, n);
        int startIndex = ind;
        boolean isFound = false;
        while(j < n){
            if(hashTable[ind].value == pair.value && hashTable[ind].key.equals(pair.key)){
                isFound = true;
                //System.out.println("Key found");
                break;
                //return prob;
            }
            else if(hashTable[ind].value == -1 && !(hashTable[ind].key.equals("DELETEDNODE"))){
                //System.out.println("Key not found");
                break;
            }
            j++;
            ind = doubleHashFunc(pair.key, j, n, 41);
            //prob++;
            if(ind == startIndex){
                break;
            }
        }
        if(!isFound){
            //System.out.println("Key not found" + "-> prob: " + j);
        }
        return new boolProb(j, isFound);
    }

    public static boolProb deleteDoubleHash(Pair[] hashTable, Pair pair, int n){
        int j = 0;
        int ind = h1(pair.key, n);
        int startIndex = ind;
        boolean isDeleted = false;
        while(j < n){
            if(hashTable[ind].value == pair.value && hashTable[ind].key.equals(pair.key)){
                isDeleted = true;
                hashTable[ind].key = "DELETEDNODE";
                hashTable[ind].value = -1;
                //System.out.println("Key Deleted");
                break;
                //return prob;
            }
            else if(hashTable[ind].value == -1 && !hashTable[ind].key.equals("DELETEDNODE")){
                System.out.println("Key not deleted because not found");
                break;
            }
            j++;
            ind = doubleHashFunc(pair.key, j, n, 41);
            //prob++;
            if(ind == startIndex){
                break;
            }
        }
        return new boolProb(j, isDeleted);
    }

    public static void doubleHashImplementationFunc(double[][] ansTableDoubleHash, int n){
        System.out.println("Double hashing");
        for(double lf = 0.4; lf <= 0.91; lf += 0.1000001) {
            int m = (int) (lf * n);
            //System.out.println(m);
            Pair[] keyValuePair = new Pair[m + 1];
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            int length = 7;
            Random rnd = new Random();
            for (int i = 1; i <= m; i++) {
                StringBuilder s = new StringBuilder();
                boolean isPresent = false;
                for (int j = 0; j < length; j++) {
                    int ind = rnd.nextInt(alphabet.length());
                    char ch = alphabet.charAt(ind);
                    s.append(ch);
                }
                //System.out.println(s);

                String str = s.toString();
                for (int j = 1; j < i; j++) {
                    if (keyValuePair[j].key.equals(str)) {
                        isPresent = true;
                        break;
                    }
                }
                if (isPresent) {
                    i--;
                } else {
                    keyValuePair[i] = new Pair(str, i);
                    //System.out.println(keyValuePair[i].key + " " + keyValuePair[i].value);
                }
            }
            System.out.println("For: " + lf);
            int[] hashIndex = new int[m + 1];
            for (int i = 1; i <= m; i++) {
                hashIndex[i] = h1(keyValuePair[i].key, n);
                //System.out.println(hashIndex[i]);
            }
            Pair[] hashTable = new Pair[n];
            for (int i = 0; i < n; i++) {

                hashTable[i] = new Pair();
            }
            boolean[] isInsertionDone = new boolean[m + 1];
            for (int i = 1; i <= m; i++) {
                isInsertionDone[i] = false;
            }
            //long totalProbInsert = 0, totalProbDelete = 0;
            int totalProbSearch1 = 0, totalProbSearch2 = 0, cntInsert = 0;
            for (int i = 1; i <= m; i++) {
                boolProb res = insertDoubleHash(hashTable, keyValuePair[i], n);
                //totalProbInsert += res.prob;
                if (res.isDone) {
                    cntInsert++;
                    isInsertionDone[i] = true;
                }
            }
            //System.out.println("Total probInsert: " + totalProbInsert);

            double searchTimeBeforeDeletion = 0.0, searchTimeAfterDeletion = 0.0;
            int l = (int) (0.1 * cntInsert);
            for (int i = 1; i <= l; i++) {
                int searchPair = rnd.nextInt(m) + 1;
                if (!isInsertionDone[searchPair]) {
                    //System.out.println("Mama eta to insert e hoini");
                    i--;
                } else {
                    double startTime = System.nanoTime();
                    boolProb res = searchDoubleHash(hashTable, keyValuePair[searchPair], n);
                    double endTime = System.nanoTime();
                    searchTimeBeforeDeletion += (endTime - startTime);
                    totalProbSearch1 += res.prob;
                }
            }
            System.out.println("Average searchProb1: " + (double)(totalProbSearch1 * 1.0 / l));
            System.out.println("Average search time before deletion: " + searchTimeBeforeDeletion / l);
            ansTableDoubleHash[(int)((lf - 0.4) * 10)][0] = (searchTimeBeforeDeletion) / l;
            ansTableDoubleHash[(int)((lf - 0.4) * 10)][1] = (double)(totalProbSearch1 * 1.0 / l);
            //Node[] deletedNodes = new Node[m + 1];
            int[] deletedIndex = new int[m + 1];
            boolean[] isDeleted = new boolean[m + 1];
            for (int i = 1; i <= m; i++) {
                isDeleted[i] = false;
            }
            for (int i = 1; i <= l; i++) {
                int deletePair = rnd.nextInt(m) + 1;
                if (isDeleted[deletePair]) {
                    i--;
                    //System.out.println("Same paisi mama");
                    continue;
                }
                boolProb res = deleteDoubleHash(hashTable, keyValuePair[deletePair], n);
                //totalProbDelete += res.prob;
                if (res.isDone) {
                    isDeleted[deletePair] = true;
                    deletedIndex[i] = deletePair;
                }
            }

            //System.out.println("Total probDelete : " + totalProbDelete);
            for (int i = 1; i <= l / 2; i++) {
                double startTime = System.nanoTime();
                boolProb res = searchDoubleHash(hashTable, keyValuePair[deletedIndex[i]], n);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                totalProbSearch2 += res.prob;
            }
            //System.out.println("SearchProb2 after only searching deleted items: " + totalProbSearch2);
            for (int i = l / 2 + 1; i <= l; i++) {
                int searchPair = rnd.nextInt(m) + 1;
                if (isDeleted[searchPair] || !isInsertionDone[searchPair]) {
                    i--;
                    //System.out.println("Koibar delete korbi vai");
                    continue;
                }
                double startTime = System.nanoTime();
                boolProb res = searchDoubleHash(hashTable, keyValuePair[searchPair], n);
                double endTime = System.nanoTime();
                searchTimeAfterDeletion += (endTime - startTime);
                totalProbSearch2 += res.prob;
            }
            System.out.println("Average searchProb2: " + (double)(totalProbSearch2 * 1.0 / l));
            System.out.println("Average search time after deletion: " + searchTimeAfterDeletion / l);
            ansTableDoubleHash[(int)((lf - 0.4) * 10)][2] = (searchTimeAfterDeletion) / l;
            ansTableDoubleHash[(int)((lf - 0.4) * 10)][3] = (double)(totalProbSearch2 * 1.0 / l);
        }
    }



    public static void main(String[] args) {
        int n;
        Scanner scn = new Scanner(System.in);
        n = scn.nextInt();

        int cnt = uniqueHash(n);
        System.out.println(cnt);
        double[][] ansTableSeparateChaining = new double[6][4];
        double[][] ansTableLinearProbing = new double[6][4];
        double[][] ansTableQuadraticProbing = new double[6][4];
        double[][] ansTableDoubleHash = new double[6][4];
        for(int i = 0; i < 6; i++){
            ansTableSeparateChaining[i] = new double[4];
            ansTableLinearProbing[i] = new double[4];
            ansTableQuadraticProbing[i] = new double[4];
            ansTableDoubleHash[i] = new double[4];
        }
        separateChainingFunc(ansTableSeparateChaining, n);
        linearProbingFunc(ansTableLinearProbing, n);
        quadraticProbingFunc(ansTableQuadraticProbing, n);
        doubleHashImplementationFunc(ansTableDoubleHash, n);

        FileWriter fw = null;
        try {
            fw = new FileWriter("Output.csv");
            fw.write("Table 1: Performance of separate chaining in various load factors\n");
            fw.write("      ,  Before deletion, After deletion\n");
            fw.write("Load factor,Avg search time,Avg search time\n");
            for(int i = 0; i < 6; i++){
                fw.write(0.4 + i * 0.1 + ",");
                for(int j = 0; j < 4; j+=2){
                    fw.write(ansTableSeparateChaining[i][j] + ",");
                }
                fw.write("\n");
            }
            fw.write("\n");
            fw.write("Table 2: Performance of linear probing in various load factors\n");
            fw.write("      ,   Before deletion,,  After deletion,\n");
            fw.write("Load factor,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            for(int i = 0; i < 6; i++){
                fw.write(0.4 + i * 0.1 + ",");
                for(int j = 0; j < 4; j++){
                    fw.write(ansTableLinearProbing[i][j] + ",");
                }
                fw.write("\n");
            }
            fw.write("\n");
            fw.write("Table 3: Performance of quadratic probing in various load factors\n");
            fw.write("      ,   Before deletion,,  After deletion,\n");
            fw.write("Load factor,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            for(int i = 0; i < 6; i++){
                fw.write(0.4 + i * 0.1 + ",");
                for(int j = 0; j < 4; j++){
                    fw.write(ansTableQuadraticProbing[i][j] + ",");
                }
                fw.write("\n");
            }
            fw.write("\n");
            fw.write("Table 4: Performance of double hashing in various load factors\n");
            fw.write("      ,   Before deletion,,  After deletion,\n");
            fw.write("Load factor,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            for(int i = 0; i < 6; i++){
                fw.write(0.4 + i * 0.1 + ",");
                for(int j = 0; j < 4; j++){
                    fw.write(ansTableDoubleHash[i][j] + ",");
                }
                fw.write("\n");
            }
            fw.write("\n");
            fw.write("Table 5: Performance of various collision resolution methods in load factor 0.4\n");
            fw.write(",Before deletion,,After deletion,\n");
            fw.write("Method,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            fw.write("Separate chaining,");
            for(int i = 0; i < 4; i++){
                if(i % 2 == 1){
                    fw.write("N/A,");
                }
                else{
                    fw.write(ansTableSeparateChaining[0][i] + ",");
                }
            }
            fw.write("\n");
            fw.write("Linear Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableLinearProbing[0][i] + ",");
            }
            fw.write("\n");
            fw.write("Quadratic Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableQuadraticProbing[0][i] + ",");
            }
            fw.write("\n");
            fw.write("Double Hashing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableDoubleHash[0][i] + ",");
            }
            fw.write("\n");
            fw.write("\n");
            fw.write("Table 6: Performance of various collision resolution methods in load factor 0.5\n");
            fw.write(",Before deletion,,After deletion,\n");
            fw.write("Method,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            fw.write("Separate chaining,");
            for(int i = 0; i < 4; i++){
                if(i % 2 == 1){
                    fw.write("N/A,");
                }
                else{
                    fw.write(ansTableSeparateChaining[1][i] + ",");
                }
            }
            fw.write("\n");
            fw.write("Linear Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableLinearProbing[1][i] + ",");
            }
            fw.write("\n");
            fw.write("Quadratic Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableQuadraticProbing[1][i] + ",");
            }
            fw.write("\n");
            fw.write("Double Hashing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableDoubleHash[1][i] + ",");
            }
            fw.write("\n");
            fw.write("\n");
            fw.write("Table 7: Performance of various collision resolution methods in load factor 0.6\n");
            fw.write(",Before deletion,,After deletion,\n");
            fw.write("Method,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            fw.write("Separate chaining,");
            for(int i = 0; i < 4; i++){
                if(i % 2 == 1){
                    fw.write("N/A,");
                }
                else{
                    fw.write(ansTableSeparateChaining[2][i] + ",");
                }
            }
            fw.write("\n");
            fw.write("Linear Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableLinearProbing[2][i] + ",");
            }
            fw.write("\n");
            fw.write("Quadratic Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableQuadraticProbing[2][i] + ",");
            }
            fw.write("\n");
            fw.write("Double Hashing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableDoubleHash[2][i] + ",");
            }
            fw.write("\n");
            fw.write("\n");
            fw.write("Table 8: Performance of various collision resolution methods in load factor 0.7\n");
            fw.write(",Before deletion,,After deletion,\n");
            fw.write("Method,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            fw.write("Separate chaining,");
            for(int i = 0; i < 4; i++){
                if(i % 2 == 1){
                    fw.write("N/A,");
                }
                else{
                    fw.write(ansTableSeparateChaining[3][i] + ",");
                }
            }
            fw.write("\n");
            fw.write("Linear Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableLinearProbing[3][i] + ",");
            }
            fw.write("\n");
            fw.write("Quadratic Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableQuadraticProbing[3][i] + ",");
            }
            fw.write("\n");
            fw.write("Double Hashing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableDoubleHash[3][i] + ",");
            }
            fw.write("\n");
            fw.write("\n");
            fw.write("Table 9: Performance of various collision resolution methods in load factor 0.8\n");
            fw.write(",Before deletion,,After deletion,\n");
            fw.write("Method,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            fw.write("Separate chaining,");
            for(int i = 0; i < 4; i++){
                if(i % 2 == 1){
                    fw.write("N/A,");
                }
                else{
                    fw.write(ansTableSeparateChaining[4][i] + ",");
                }
            }
            fw.write("\n");
            fw.write("Linear Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableLinearProbing[4][i] + ",");
            }
            fw.write("\n");
            fw.write("Quadratic Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableQuadraticProbing[4][i] + ",");
            }
            fw.write("\n");
            fw.write("Double Hashing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableDoubleHash[4][i] + ",");
            }
            fw.write("\n");
            fw.write("\n");
            fw.write("Table 10: Performance of various collision resolution methods in load factor 0.9\n");
            fw.write(",Before deletion,,After deletion,\n");
            fw.write("Method,Avg search time,Avg number of probes,Avg search time,Avg number of probes\n");
            fw.write("Separate chaining,");
            for(int i = 0; i < 4; i++){
                if(i % 2 == 1){
                    fw.write("N/A,");
                }
                else{
                    fw.write(ansTableSeparateChaining[5][i] + ",");
                }
            }
            fw.write("\n");
            fw.write("Linear Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableLinearProbing[5][i] + ",");
            }
            fw.write("\n");
            fw.write("Quadratic Probing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableQuadraticProbing[5][i] + ",");
            }
            fw.write("\n");
            fw.write("Double Hashing,");
            for(int i = 0; i < 4; i++){
                fw.write(ansTableDoubleHash[5][i] + ",");
            }
            fw.write("\n");
            fw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
