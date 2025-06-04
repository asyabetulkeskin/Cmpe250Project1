import java.io.PrintStream;

public class AvlTree {
    public static class Node{
        public ParkingLot parkingLot;
        public Node left;
        public Node right;
        public Node parent;
        public int height;
        Node(ParkingLot parkingLot){
            this.parkingLot=parkingLot;
            height=0;
        }

    }
    public Node root;
    public int getHeight(Node node){
        if (node==null)
            return -1;
        else return node.height;
    }

    //this function is needed to decide if the tree needs rotation
    public int getBalanceFactor(Node node){
        return getHeight(node.left)-getHeight(node.right);

    }

    public int getCapacityConstraint(Node node){return node.parkingLot.capacityConstraint;}


    //in the left-left imbalance cases this function is used for rotation.
    public Node rightRotation(Node node){
        // rotation is made between the node in which imbalance occurred, the left child of the node, and also the left child of the left child.
        // the node becomes the child of its left child and node's left child becomes the right child of its previous left child
        // previous left child of the node becomes the new parent among three nodes and its right child becomes the node
        // parents are updated according to the situation
        Node parentOfNode = node.parent;
        Node leftOfNode = node.left;
        Node rightOfLeft = leftOfNode.right;
        leftOfNode.right=node;
        node.left=rightOfLeft;
        leftOfNode.parent = parentOfNode;
        node.parent = leftOfNode;

        if (parentOfNode!=null){ // if the node with imbalance is not the root of the tree, the parent of the node must be assigned to the new parent node of the rotation
            if (parentOfNode.right==node){parentOfNode.right=leftOfNode;}
            else if (parentOfNode.left==node){parentOfNode.left=leftOfNode;}
        }

        if (rightOfLeft!=null){rightOfLeft.parent=node;}

        //the height of the node in which imbalance occurred and the height of the new parent node among three nodes is updated, the height of the other is not changed so does not need updating
        node.height=1+Math.max(getHeight(node.right),getHeight(node.left));
        leftOfNode.height=1+Math.max(getHeight(leftOfNode.right),getHeight(leftOfNode.left));
        return leftOfNode; // new parent of the three node among which rotation has occurred is the left child of the node in which imbalance occurred
    }


    //in the right-right imbalance cases this function is used for rotation.
    public Node leftRotation(Node node){
        // rotation is made between the node in which imbalance occurred, the right child of the node, and also the right child of the right child.
        // the node becomes the child of its right child and node's right child becomes the left child of its previous right child
        // previous right child of the node becomes the new parent among three nodes and its left child becomes the node
        // parents are updated according to the situation
        Node parentOfNode = node.parent;
        Node rightOfNode = node.right;
        Node leftOfRight=rightOfNode.left;
        rightOfNode.left=node;
        node.right=leftOfRight;
        rightOfNode.parent=parentOfNode;
        node.parent=rightOfNode;

        if (parentOfNode!=null){ // if the node with imbalance is not the root of the tree, the parent of the node must be assigned to the new parent node of the rotation
            if (parentOfNode.right==node){parentOfNode.right=rightOfNode;}
            else if (parentOfNode.left==node){parentOfNode.left=rightOfNode;}
        }

        if (leftOfRight!=null){leftOfRight.parent=node;}

        //the height of the node in which imbalance occurred and the height of the new parent node among three nodes is updated, the height of the other is not changed so does not need updating
        node.height=1+Math.max(getHeight(node.right),getHeight(node.left));
        rightOfNode.height=1+Math.max(getHeight(rightOfNode.right),getHeight(rightOfNode.left));
        return rightOfNode; // new parent of the three node among which rotation has occurred is the right child of the node in which imbalance occurred
    }



    public Node insert(ParkingLot parkingLot, Node root){
        if (root==null){
            root = new Node(parkingLot);
        }

        //traversing until reaching a leaf

        else if (parkingLot.capacityConstraint>getCapacityConstraint(root)){
            root.right = insert(parkingLot, root.right);
            root.right.parent=root;
        }
        else if (parkingLot.capacityConstraint<getCapacityConstraint(root)){
            root.left= insert(parkingLot, root.left);
            root.left.parent=root;
        }
        else return root;

        //updating the height of the root
        root.height=1+Math.max(getHeight(root.right),getHeight(root.left));

        int balanceFactor = getBalanceFactor(root);

        //if needed, balancing among 4 possible rotations

        if (balanceFactor==2 && getBalanceFactor(root.left)==1){ //Left-left rotation condition
            return rightRotation(root);
        }
        if (balanceFactor==-2 && getBalanceFactor(root.right)==-1){ //Right-right rotation condition
            return leftRotation(root);
        }
        if (balanceFactor==2 && getBalanceFactor(root.left)==-1){ //Left-right rotation condition
            root.left=leftRotation(root.left);
            return rightRotation(root);
        }
        if (balanceFactor==-2 && getBalanceFactor(root.right)==1){ //Right-left rotation condition
            root.right=rightRotation(root.right);
            return leftRotation(root);
        }
        return root;

    }

    public Node delete(ParkingLot parkingLot, Node root){
        if (root==null){return root;}
        if (getCapacityConstraint(root)==parkingLot.capacityConstraint){
            if (root.left==null && root.right==null){ //leaf condition

                //updating parent and child
                if (root.parent!=null && getCapacityConstraint(root.parent)<getCapacityConstraint(root)){
                    root.parent.right=null;
                }
                else if (root.parent!=null && getCapacityConstraint(root.parent)>getCapacityConstraint(root)){
                    root.parent.left=null;}
                if (root.parent!=null){
                    root.parent.height=1+Math.max(getHeight(root.parent.right), getHeight(root.parent.left));
                }
                root=null;
                return root;
            }
            else if (root.right!=null && root.left==null) {//only right child
                if (root.parent!=null){

                    //updating parent and child
                    if (getCapacityConstraint(root.parent)>getCapacityConstraint(root)){root.parent.left=root.right;}
                    else if (getCapacityConstraint(root.parent)<getCapacityConstraint(root)){root.parent.right=root.right;}
                    root.parent.height=1+Math.max(getHeight(root.parent.right), getHeight(root.parent.left));
                }
                root.right.parent=root.parent;

                //balancing if needed
                int balanceFactor = getBalanceFactor(root.right);

                if (balanceFactor==2 && getBalanceFactor(root.right.left)==1){ //LL
                    root.right= rightRotation(root.right.right);
                }
                if (balanceFactor==-2 && getBalanceFactor(root.right.right)==-1){ //RR
                    root.right= leftRotation(root.right.right);
                }
                if (balanceFactor==2 && getBalanceFactor(root.right.left)==-1){ //LR
                    root.left=leftRotation(root.right.left);
                    root.right= rightRotation(root.right.right);
                }
                if (balanceFactor==-2 && getBalanceFactor(root.right.right)==1){ //RL
                    root.right=rightRotation(root.right.right);
                    root.right= leftRotation(root.right.right);
                }
                return root.right;
            }
            else if (root.right==null){//only left child
                if (root.parent!=null){

                    //updating parent and child
                    if (getCapacityConstraint(root.parent)>getCapacityConstraint(root)){root.parent.left=root.left;}
                    else if (getCapacityConstraint(root.parent)<getCapacityConstraint(root)){root.parent.right=root.left;}
                    root.parent.height=1+Math.max(getHeight(root.parent.right), getHeight(root.parent.left));
                }
                root.left.parent=root.parent;

                //balancing if needed
                int balanceFactor = getBalanceFactor(root.left);

                if (balanceFactor==2 && getBalanceFactor(root.left.left)==1){ //LL
                    root.left= rightRotation(root.left.right);
                }
                if (balanceFactor==-2 && getBalanceFactor(root.left.right)==-1){ //RR
                    root.left= leftRotation(root.left.right);
                }
                if (balanceFactor==2 && getBalanceFactor(root.left.left)==-1){ //LR
                    root.left=leftRotation(root.left.left);
                    root.left= rightRotation(root.left.right);
                }
                if (balanceFactor==-2 && getBalanceFactor(root.left.right)==1){ //RL
                    root.right=rightRotation(root.left.right);
                    root.left= leftRotation(root.left.right);
                }
                return root.left;
            }

            //if both right and left child exists, finding the node with minimum capacity constraint parking lot in the right subtree
            Node current = root;
            current=current.right;
            while (current.left!=null){
                current=current.left;
            }

            //deleting the node of min value parking lot in the right subtree, and replacing the parking lot of current node with this parking lot that has min value
            ParkingLot parkingLotToHold = current.parkingLot;
            root.right=delete(current.parkingLot,root.right);
            root.parkingLot=parkingLotToHold;

            //balancing if needed
            int balanceFactor = getBalanceFactor(root);

            if (balanceFactor==2 && getBalanceFactor(root.left)==1){ //LL
                root= rightRotation(root);
            }
            if (balanceFactor==-2 && getBalanceFactor(root.right)==-1){ //RR
                root= leftRotation(root);
            }
            if (balanceFactor==2 && getBalanceFactor(root.left)==-1){ //LR
                root.left=leftRotation(root.left);
                root= rightRotation(root);
            }
            if (balanceFactor==-2 && getBalanceFactor(root.right)==1){ //RL
                root.right=rightRotation(root.right);
                root= leftRotation(root);
            }
        }

        //if the node to be deleted is not found, traverse in the tree and use balancing if needed

        else if (parkingLot.capacityConstraint>getCapacityConstraint(root)){
            root.right = delete(parkingLot,root.right);
            int balanceFactor = getBalanceFactor(root);

            if (balanceFactor==2 && getBalanceFactor(root.left)==1){ //LL
                root= rightRotation(root);
            }
            if (balanceFactor==-2 && getBalanceFactor(root.right)==-1){ //RR
                root= leftRotation(root);
            }
            if (balanceFactor==2 && getBalanceFactor(root.left)==-1){ //LR
                root.left=leftRotation(root.left);
                root= rightRotation(root);
            }
            if (balanceFactor==-2 && getBalanceFactor(root.right)==1){ //RL
                root.right=rightRotation(root.right);
                root= leftRotation(root);
            }
        }
        else if (parkingLot.capacityConstraint<getCapacityConstraint(root)){
            root.left = delete(parkingLot,root.left);
            int balanceFactor = getBalanceFactor(root);

            if (balanceFactor==2 && getBalanceFactor(root.left)==1){ //LL
                root= rightRotation(root);
            }
            if (balanceFactor==-2 && getBalanceFactor(root.right)==-1){ //RR
                root= leftRotation(root);
            }
            if (balanceFactor==2 && getBalanceFactor(root.left)==-1){ //LR
                root.left=leftRotation(root.left);
                root= rightRotation(root);
            }
            if (balanceFactor==-2 && getBalanceFactor(root.right)==1){ //RL
                root.right=rightRotation(root.right);
                root= leftRotation(root);
            }
        }
        if (root!=null){root.height=1+Math.max(getHeight(root.right),getHeight(root.left));}
        return root;
    }



    //this function is called in Main to create new parking lot with given capacity constraint
    public void createNewParkingLot(int capacityConstraint, int truckLimit){
        ParkingLot parkingLot = new ParkingLot(capacityConstraint, truckLimit);
        root = insert(parkingLot, root);
    }

    //this function is called in Main to delete the parking lot with given capacity constraint
    public void deleteParkingLot(int capacityConstraint){
        Node node=findNode(capacityConstraint,root);
        ParkingLot parkingLot = node.parkingLot;
        root= delete(parkingLot, root);
    }


    //searches for a node with parking lot containing given capacity constraint, if it can be found, gives the node, otherwise gives null
    public Node findNode(int capacityConstraint,Node root){
        if (root==null){
            return null;
        }
        if (getCapacityConstraint(root)==capacityConstraint){
            return root;
        }
        if (capacityConstraint>getCapacityConstraint(root)) {
            return findNode(capacityConstraint, root.right);
        }
        else
            return findNode(capacityConstraint, root.left);
    }

    //returns the node with the parking lot which has minimum capacity constraint
    public Node findMin(Node root){
        if (root==null)
            return null;
        if (root.left==null)
            return root;
        else
            return findMin(root.left);
    }

    //returns the node with the parking lot which has maximum capacity constraint
    public Node findMax(Node root){
        if (root==null)
            return null;
        if (root.right==null)
            return root;
        else
            return findMax(root.right);

    }


    //when a parking lot with given capacity does not exist in the tree, this function returns the node after which the node would be inserted if it was inserted
    public Node whereToInsert(int capacityConstraint, Node root){
        if (root==null) return null;
        if (root.left==null && capacityConstraint<getCapacityConstraint(root))
            return root;
        if (root.right==null && capacityConstraint>getCapacityConstraint(root))
            return root;
        if (capacityConstraint>getCapacityConstraint(root)) {
            return whereToInsert(capacityConstraint, root.right);
        }
        if (capacityConstraint<getCapacityConstraint(root)) {
            return whereToInsert(capacityConstraint, root.left);
        }
        return null;
    }




    //algorithm for counting the trucks in the tree which are placed in parking lots with greater capacity constraint than given capacity
    //holder[] holds the total number of trucks
    public int traverseForCount(Node root, int capacity, int[] holder, Node parentOfRoot){
        if (root==null) { return holder[0];} // if the root becomes null, there is no other truck to add

        //counting the truck number of the current node if the capacity constraint of the parking lot it holds is greater than the given capacity
        if (getCapacityConstraint(root) > capacity) {
            int currSum= root.parkingLot.waitingSection.size()+root.parkingLot.readySection.size();
            holder[0] = holder[0]+currSum;
        }

        // afterwards calculating the total number of trucks in the right subtree if it exists
        if (root.right!=null && getCapacityConstraint(root)>capacity){

            Node minInRight=findMin(root.right);
            Node rootRight = root.right;
            rootRight.parent=null; //this is made to prevent traversing same nodes since the algorithm checks also the parent nodes
            if (minInRight!=null){traverseForCount(minInRight, capacity,holder, minInRight.parent);}
            rootRight.parent=root; //reconnecting for accurate tree structure

        }

        // applying the same procedure to the parent node of the root
        if (parentOfRoot!=null && getCapacityConstraint(root.parent)!=capacity){traverseForCount(root.parent,capacity,holder,parentOfRoot.parent);}
        return holder[0];
    }


    //this function is called in Main for counting the trucks
    public int truckCount(int capacity){
        int[] sumHolder = new int[1];
        Node aim = findNode(capacity,root);
        if (aim!=null){ // if a parking lot with given capacity constraint exists, starting the procedure from the existing node
            Node minInRight = findMin(aim.right);
            if (minInRight!=null){ // first, counting the trucks in the right subtree if right subtree exists
                sumHolder[0] = traverseForCount(minInRight,capacity,sumHolder,minInRight.parent);
            }
            if (aim.parent!=null){ // second, continuing from the parent node if the parent exists
                sumHolder[0]=traverseForCount(aim.parent,capacity,sumHolder,aim.parent.parent);
            }

            return sumHolder[0];
        }

        // if the parking lot with given capacity does not exist, finding the node after which it would be inserted, then starting the procedure from that node
        Node whereTo= whereToInsert(capacity,root);
        if (whereTo!=null){return traverseForCount(whereTo,capacity,sumHolder,whereTo.parent);}
        else return sumHolder[0];

    }

    //algorithm for checking if there are any trucks in the waiting section of a parking lot with greater capacity constraint than given capacity
    //if so, putting the truck in ready position and returning the node that parking lot exists
    public Node traverseForLarger(Node root, int capacity, Node parentOfRoot){
        if (root==null) { return null;}

        //checking and if so adding a truck from waiting section to the ready section of the current node if the capacity constraint of the parking lot it holds is greater than the given capacity
        Node isAdded = null;
        if (!root.parkingLot.waitingSection.isEmpty() && getCapacityConstraint(root) > capacity) {
            Truck truck = root.parkingLot.waitingSection.getFirst();
            root.parkingLot.readySection.add(truck);
            root.parkingLot.waitingSection.removeFirst();
            isAdded = root;
        }

        //if the truck is not put in ready position, the greater capacity constraint parking lots are checked in the right subtree of the current root, if the right subtree exists
        if (isAdded==null) {
            if (root.right!=null && getCapacityConstraint(root)>capacity){
                Node minInRight=findMin(root.right);
                Node rootRight = root.right;
                rootRight.parent=null; // this is made to assure the traverse only happens in the subtree since the algorithm checks also the parent nodes
                if (minInRight!=null){isAdded = traverseForLarger(minInRight, capacity,minInRight.parent);}
                rootRight.parent=root; //reconnecting for accurate tree structure
            }
        }

        //if a truck still is not put in ready position, applying the same procedure to the parent node of the root
        if (isAdded==null && parentOfRoot!=null && getCapacityConstraint(root.parent)!=capacity) {
            isAdded = traverseForLarger(root.parent, capacity,parentOfRoot.parent);
        }
        return isAdded;
    }

    //this function is called in Main for putting a truck in ready position
    public Node readyCommand(int capacity){
        Node aim = findNode(capacity, root);
        if (aim!=null){ // if a parking lot with given capacity constraint exists, starting the procedure from the existing node
            if (!aim.parkingLot.waitingSection.isEmpty()){ // checking if a truck can be added from waiting section to ready section of the parking lot with exact same capacity given
                Truck truck = aim.parkingLot.waitingSection.getFirst();
                aim.parkingLot.readySection.add(truck);
                aim.parkingLot.waitingSection.removeFirst();
                return aim;
            }
            else {
                Node ifAdded = null;
                Node minInRight = findMin(aim.right);
                if (minInRight!=null){ // if a node is not found, first looking at the parking lots in the right subtree if right subtree exists
                    ifAdded = traverseForLarger(minInRight,capacity,minInRight.parent);
                }
                if (ifAdded==null && aim.parent!=null){ // if still not found, second continuing from the parent node if the parent exists
                    ifAdded=traverseForLarger(aim.parent,capacity,aim.parent.parent);
                }

                return ifAdded; // if any truck could not be put to the ready position, returns null, otherwise it returns the node in which a truck could be added to the ready section of the parking lot
            }
        }

        // if the parking lot with given capacity does not exist, finding the node after which it would be inserted, then starting the procedure from that node
        Node whereTo= whereToInsert(capacity,root);
        if (whereTo!=null){return traverseForLarger(whereTo,capacity,whereTo.parent);}
        else return null; // if any truck could not be put to the ready position, returns null, otherwise it returns the node in which a truck could be added to the ready section of the parking lot
    }

    //This function is the algorithm for adding a new truck
    //checking if there is a space in a parking lot with smaller capacity constraint than given capacity
    //if so, putting the truck in that parking lot and returning the node that parking lot exists
    public Node traverseForSmaller(Node root, int ID, int capacity, Node parentOfRoot){
        if (root==null) { return null;}

        //checking and if so adding new truck to the waiting section of the current node if the capacity constraint of the parking lot it holds is smaller than the given capacity
        Node isAdded = null;
        if (root.parkingLot.empty() && getCapacityConstraint(root) < capacity) {
            root.parkingLot.adding(ID, capacity);
            isAdded = root;
        }

        //if the truck is not added, the smaller capacity constraint parking lots are checked in the left subtree of the current root, if the left subtree exists
        if (isAdded==null) {
            if (root.left!=null && getCapacityConstraint(root)<capacity){
                Node maxInLeft = findMax(root.left);
                Node rootLeft = root.left;
                rootLeft.parent=null; // this is made to assure the traverse only happens in the subtree since the algorithm checks also the parent nodes
                if (maxInLeft!=null){isAdded = traverseForSmaller(maxInLeft, ID, capacity,maxInLeft.parent);}
                rootLeft.parent=root; //reconnecting for accurate tree structure
            }
        }

        //if the truck still not added, applying the same procedure to the parent node of the root
        if (isAdded==null && parentOfRoot!=null && getCapacityConstraint(parentOfRoot)!=capacity) {
            isAdded = traverseForSmaller(root.parent, ID, capacity,parentOfRoot.parent);
        }
        return isAdded;
    }


    //this function is called in Main for adding a new truck
    public int addingNewTruck(int truckId, int capacity){
        if (capacity<getCapacityConstraint(findMin(root))) return -1;
        Node aim = findNode(capacity, root);
        if (aim!=null) { // if a parking lot with given capacity constraint exists, starting the procedure from the existing node
            if (aim.parkingLot.empty()) { // checking if the truck can be added to the parking lot with exact same capacity given
                aim.parkingLot.adding(truckId, capacity);
                return capacity;
            }
            else {
                Node ifAdded=null;
                Node maxInLeft = findMax(aim.left);
                if (maxInLeft!=null){ // if a node is not found, first looking at the parking lots in the left subtree if left subtree exists
                    ifAdded = traverseForSmaller(maxInLeft,truckId,capacity,maxInLeft.parent);
                }
                if (ifAdded==null && aim.parent!=null){ // if still not found, second continuing from the parent node if the parent exists
                    ifAdded=traverseForSmaller(aim.parent,truckId,capacity,aim.parent.parent);
                }
                if (ifAdded==null){return -1;} // if the truck can not be added to any parking lot, the function returns -1
                else return getCapacityConstraint(ifAdded); // if the truck can be added, it returns the capacity constraint of the parking lot in which it is placed
            }
        }
        else { // if the parking lot with given capacity does not exist, finding the node after which it would be inserted, then starting the procedure from that node
            Node whereTo= whereToInsert(capacity,root);
            Node ifAdded=null;
            if(whereTo!=null){ifAdded=traverseForSmaller(whereTo,truckId,capacity,whereTo.parent);}
            if (ifAdded==null){return -1;} // if the truck can not be added to any parking lot, the function returns -1
            else return getCapacityConstraint(ifAdded); // if the truck can be added, it returns the capacity constraint of the parking lot in which it is placed
        }

    }


    //This function is the algorithm for replacing an existing truck
    //checking if there is a space in a parking lot with smaller capacity constraint than given capacity
    //if so, putting the truck in that parking lot and returning the node that parking lot exists
    public Node traverseForRelocate(Node root,Truck truck,int capacity,Node parentOfRoot){
        if (root==null) { return null;}

        //checking and if so adding the truck to the waiting section of the current node if the capacity constraint of the parking lot it holds is smaller than the given capacity
        Node isAdded = null;
        if (root.parkingLot.empty() && getCapacityConstraint(root) < capacity) {
            root.parkingLot.waitingSection.add(truck);
            isAdded = root;
        }

        //if the truck is not added, the smaller capacity constraint parking lots are checked in the left subtree of the current root, if the left subtree exists
        if (isAdded==null) {
            if (root.left!=null && getCapacityConstraint(root)<capacity){
                Node maxInLeft = findMax(root.left);
                Node rootLeft = root.left;
                rootLeft.parent=null; // this is made to assure the traverse only happens in the subtree since the algorithm checks also the parent nodes
                if (maxInLeft!=null){ isAdded = traverseForRelocate(maxInLeft, truck, capacity,maxInLeft.parent);}
                rootLeft.parent=root; // reconnecting for accurate tree structure
            }
        }

        //if the truck still not added, applying the same procedure to the parent node of the root
        if (isAdded==null && parentOfRoot!=null && getCapacityConstraint(root.parent)!=capacity) {
            isAdded = traverseForRelocate(root.parent, truck, capacity,parentOfRoot.parent);
        }
        return isAdded;
    }

    // This function relocates existing trucks with their remaining capacity values
    public int relocateTruck(Truck truck, int capacity){
        if (capacity<getCapacityConstraint(findMin(root))) return -1;
        Node aim = findNode(capacity, root);
        if (aim!=null) { // if a parking lot with given capacity constraint exists, starting the procedure from the existing node
            if (aim.parkingLot.empty()) { // checking if the truck can be added to the parking lot with exact same capacity given
                aim.parkingLot.waitingSection.add(truck);
                return capacity;
            }
            else {
                Node ifAdded=null;
                Node maxInLeft = findMax(aim.left);
                if (maxInLeft!=null){ // if a node is not found, first looking at the parking lots in the left subtree if left subtree exists
                    ifAdded = traverseForRelocate(maxInLeft,truck,capacity,maxInLeft.parent);
                }
                if (ifAdded==null && aim.parent!=null){ // if still not found, second continuing from the parent node if the parent exists
                    ifAdded=traverseForRelocate(aim.parent,truck,capacity,aim.parent.parent);
                }
                if (ifAdded==null){return -1;} // if the truck can not be added to any parking lot, the function returns -1
                else return getCapacityConstraint(ifAdded); // if the truck can be added, it returns the capacity constraint of the parking lot in which it is placed
            }
        }

        // if the parking lot with given capacity does not exist, finding the node after which it would be inserted, then starting the procedure from that node
        Node whereTo= whereToInsert(capacity,root);
        Node ifAdded = null;
        if (whereTo!=null){ifAdded=traverseForRelocate(whereTo,truck,capacity,whereTo.parent);}
        if (ifAdded==null){return -1;} // if the truck can not be added to any parking lot, the function returns -1
        else return getCapacityConstraint(ifAdded); // if the truck can be added, it returns the capacity constraint of the parking lot in which it is placed
    }


    //This function checks whether there exists any trucks placed in the ready section of the parking lots with capacity constraint greater than given capacity
    public boolean ifReadyTrucks(Node root, int capacity, Node parentOfRoot){
        if (root==null) { return false;}

        //checking if there exists a truck in the ready section of the current node if the capacity constraint of the parking lot it holds is greater than the given capacity
        boolean isReady = false;
        if (!root.parkingLot.readySection.isEmpty() && getCapacityConstraint(root) > capacity){isReady=true;}

        //if a truck in ready position is not found, the greater capacity constraint parking lots are checked in the right subtree of the current root, if the right subtree exists
        if (!isReady){
            if (root.right!=null && getCapacityConstraint(root)>capacity){
                Node minInRight=findMin(root.right);
                Node rootRight = root.right;
                rootRight.parent=null; // this is made to assure the traverse only happens in the subtree since the algorithm checks also the parent nodes
                if (minInRight!=null){isReady = ifReadyTrucks(minInRight, capacity,minInRight.parent);}
                rootRight.parent=root; //reconnecting for accurate tree structure
            }
        }

        //if still a truck is not found in any ready position, applying the same procedure to the parent node of the root
        if (!isReady && parentOfRoot!=null && getCapacityConstraint(root.parent)!=capacity){
            isReady=ifReadyTrucks(root.parent,capacity,parentOfRoot.parent);
        }
        return isReady; // if any parking lot with a ready truck exists, returns true and otherwise false

    }


    //algorithm for checking if the given load can be loaded to any truck in ready position in a parking lot with greater capacity constraint than given capacity
    //if so, loading the truck, relocating the truck and continuing the process with remaining load
    //holder[] holds the remaining load
    //"leader" refers to the parent node of the right subtree which will be traversed in the process
    public void loadAlgorithm(Node root, int capacity, int[] holder, PrintStream outStream, Node parentOfRoot, Node leader){
        if (root==null) {
            return ;}
        if (holder[0]==0) {return ;} // if no load exists, do nothing

        //checking the current node whether the capacity constraint of the parking lot it holds is greater than the given capacity
        if (getCapacityConstraint(root) > capacity) {
            while (!root.parkingLot.readySection.isEmpty()){ // The trucks in the ready section of the parking lot of current root takes load until the ready section includes no truck
                if (holder[0]>=getCapacityConstraint(root)){

                    // taking the first truck in the ready section, removing it from the ready section, updating its load
                    Truck truckBeingLoaded = root.parkingLot.readySection.removeFirst();
                    truckBeingLoaded.load+=getCapacityConstraint(root);
                    int newLotCapacity;

                    // if the updated load is equal to the maximum capacity of the truck, truck is emptied and its load becomes zero
                    // the truck is relocated with its updated remaining capacity, which is its max capacity
                    if (truckBeingLoaded.load==truckBeingLoaded.maxCapacity){
                        truckBeingLoaded.load=0;
                        newLotCapacity = relocateTruck(truckBeingLoaded,truckBeingLoaded.maxCapacity);
                    }

                    // if the updated load is not equal to the max capacity of the truck, the remaining capacity becomes the difference between max capacity and the updated load
                    // the truck is relocated with this remaining capacity
                    else {newLotCapacity = relocateTruck(truckBeingLoaded,truckBeingLoaded.maxCapacity-truckBeingLoaded.load);}

                    outStream.print(truckBeingLoaded.ID+" "+newLotCapacity);

                    holder[0] =holder[0]-getCapacityConstraint(root);
                    if (holder[0]==0){
                        outStream.println();
                        break;
                    }

                    //this part is to check if the remaining load can be loaded to any truck, for this it is checked if there are any ready trucks
                    //if there is, then load process will continue and " - " will be added to the line, otherwise load process will not be able to continue so nothing else will be written
                    boolean ifReady=false;
                    Node min = findMin(root.right);
                    if (min!=null){
                        ifReady = ifReadyTrucks(min,getCapacityConstraint(root),min.parent);
                    }
                    if (!ifReady){
                        if (root.parent!=null)
                            ifReady = ifReadyTrucks(root.parent,getCapacityConstraint(root),root.parent.parent);
                    }
                    if (ifReady || !root.parkingLot.readySection.isEmpty())
                        outStream.print(" - ");
                    else {outStream.println();}
                }
                else{ // if the load is smaller than the capacity constraint of the root, that means the first ready truck will take all the load and remaining load will be zero
                    Truck truckBeingLoaded = root.parkingLot.readySection.removeFirst();
                    truckBeingLoaded.load+=+holder[0]; // the truck takes all the remaining load

                    // the truck is relocated with this remaining capacity
                    int newLotCapacity = relocateTruck(truckBeingLoaded,truckBeingLoaded.maxCapacity-truckBeingLoaded.load);
                    outStream.print(truckBeingLoaded.ID+" "+newLotCapacity);
                    holder[0]=0;
                    outStream.println();
                    break; //since loadAmount is 0
                }

            }
        }

        //if there is still remaining load, the greater capacity constraint parking lots are checked in the right subtree of the current root, if the right subtree exists
        if (holder[0]!=0) {
            if (root.right != null && getCapacityConstraint(root) > capacity) {
                Node minInRight=findMin(root.right);
                if (minInRight!=null){loadAlgorithm(minInRight, capacity,holder,outStream,minInRight.parent,root);}
            }
        }

        //if there is still remaining load, applying the same procedure to the parent node of the root
        if (holder[0]!=0 && parentOfRoot!=null && getCapacityConstraint(root.parent)!=capacity && root.parent!=leader){
            loadAlgorithm(root.parent,capacity,holder,outStream,parentOfRoot.parent,leader);
        }

    }


    //This function is called in Main for loading
    public void load(int capacity, int loadAmount,PrintStream outStream){
        if (loadAmount==0){return;}
        Node aim = findNode(capacity, root);
        int[] remainingLoadHolder = new int[1];
        remainingLoadHolder[0]=loadAmount;
        if (aim!=null){ // if a parking lot with given capacity constraint exists, starting the procedure from the existing node

            while (!aim.parkingLot.readySection.isEmpty()){ // The trucks in the ready section of the parking lot with exact same capacity given take load until the ready section includes no truck

                if (remainingLoadHolder[0]>=capacity){

                    // taking the first truck in the ready section, removing it from the ready section, updating its load
                    Truck truckBeingLoaded = aim.parkingLot.readySection.removeFirst();
                    truckBeingLoaded.load+=capacity;
                    int newLotCapacity;

                    // if the updated load is equal to the maximum capacity of the truck, truck is emptied and its load becomes zero
                    // the truck is relocated with its updated remaining capacity, which is its max capacity
                    if (truckBeingLoaded.load==truckBeingLoaded.maxCapacity){
                        truckBeingLoaded.load=0;
                        newLotCapacity = relocateTruck(truckBeingLoaded,truckBeingLoaded.maxCapacity);
                    }

                    // if the updated load is not equal to the max capacity of the truck, the remaining capacity becomes the difference between max capacity and the updated load
                    // the truck is relocated with this remaining capacity
                    else {newLotCapacity = relocateTruck(truckBeingLoaded,truckBeingLoaded.maxCapacity-truckBeingLoaded.load);}

                    outStream.print(truckBeingLoaded.ID+" "+newLotCapacity);
                    remainingLoadHolder[0] =remainingLoadHolder[0]-capacity;

                    if (remainingLoadHolder[0]==0){
                        outStream.println();
                        break;
                    }

                    //this part is to check if the remaining load can be loaded to any truck, for this it is checked if there are any ready trucks
                    //if there is, then load process will continue and " - " will be added to the line, otherwise load process will not be able to continue so nothing else will be written
                    boolean ifReady=false;
                    Node min = findMin(aim.right);
                    if (min!=null){
                        ifReady = ifReadyTrucks(findMin(aim.right),getCapacityConstraint(aim),findMin(aim.right).parent);
                    }
                    if (!ifReady){
                        if (aim.parent!=null)
                            ifReady = ifReadyTrucks(aim.parent,getCapacityConstraint(aim),aim.parent.parent);
                    }
                    if (ifReady || !aim.parkingLot.readySection.isEmpty())
                        outStream.print(" - ");
                    else {outStream.println();}
                }
                else{ // if the load is smaller than the capacity constraint of the parking lot "aim" holds, that means the first ready truck will take all the load and remaining load will be zero
                    Truck truckBeingLoaded = aim.parkingLot.readySection.removeFirst();
                    truckBeingLoaded.load+=remainingLoadHolder[0]; // the truck takes all the remaining load

                    // the truck is relocated with this remaining capacity
                    int newLotCapacity = relocateTruck(truckBeingLoaded,truckBeingLoaded.maxCapacity-truckBeingLoaded.load);
                    outStream.print(truckBeingLoaded.ID+" "+newLotCapacity);
                    remainingLoadHolder[0]=0;
                    outStream.println();
                    break;//since loadAmount is 0
                }
            }

            if (remainingLoadHolder[0]!=0){
                Node minInRight = findMin(aim.right);
                if (minInRight!=null){ // if load remains, first looking at the parking lots in the right subtree if right subtree exists
                    loadAlgorithm(minInRight,capacity,remainingLoadHolder,outStream,minInRight.parent,null);
                }
                if (remainingLoadHolder[0]!=0 && aim.parent!=null){ // if load still remains, second continuing from the parent node if the parent exists
                    loadAlgorithm(aim.parent,capacity,remainingLoadHolder,outStream,aim.parent.parent,null);
                }
            }

            //if the starting and ending load is the same, no loading happened, output is zero
            if (remainingLoadHolder[0]==loadAmount){outStream.println(-1);
            }
        }
        else {

            // if the parking lot with given capacity does not exist, finding the node after which it would be inserted, then starting the procedure from that node
            Node whereTo = whereToInsert(capacity, root);
            if (whereTo != null) {
                loadAlgorithm(whereTo, capacity, remainingLoadHolder, outStream, whereTo.parent,null);
            }

            //if the starting and ending load is the same, no loading happened, output is zero
            if (loadAmount==remainingLoadHolder[0]){outStream.println(-1);}
        }
    }
}
