import common;

struct KDTree(size_t dim){
    alias Pt = Point!dim;
    KDNode!0 root;

    //root node starting with 0th dimension as the splitting dimension
    this(Pt[] points){
        root = new KDNode!0(points);
    }

    //node class takes a parameter for specifying which dimeension it splits on 
    class KDNode(size_t split_dimension){
        enum this_level = split_dimension;
        enum next_level = (split_dimension +1 ) % dim;
        public KDNode!next_level leftChild, rightChild;
        public Pt splitPoint;

        this(Pt[] points){
        
            //base case for if only 1 point
            if(points.length == 1){
                splitPoint = points[0];
            }else{

                //sorting by the left half
                Pt[] leftHalf = medianByDimension!(this_level, dim)(points);
                splitPoint = points[leftHalf.length];
                leftChild = new KDNode!next_level(leftHalf);
                if (points.length > 2){
                    rightChild = new KDNode!next_level(points[leftHalf.length + 1 .. $]);
                }
            }
            
        }
    }

    Pt[] rangeQuery( Pt p, float r ){
        Pt[] range;

        void RangeRecurse( NodeType )( NodeType n ){

            if (distance(p, n.splitPoint) <= r){
                range ~= n.splitPoint;

                if(n.leftChild !is null && p[n.next_level] - r <= n.leftChild.splitPoint[n.next_level]){
                    RangeRecurse(n.leftChild);
                }
                if(n.rightChild !is null && p[n.next_level] + r >= n.rightChild.splitPoint[n.next_level]){
                    RangeRecurse(n.rightChild);
                }
            }      
        
        }
        return range;
    }

    Pt[] KNNQuery( Pt point, int K){
        auto pq = makePriorityQueue!dim(point);
        //aabb starts with infinity bounds to then be narrowed down
        AABB!dim aabb;
        aabb.min[] = -float.infinity;
        aabb.max[] = float.infinity;

        void KnnRecurse(NodeType)(NodeType n, AABB!dim tempBox){
            //if pq is less than the K of neighbors to find
            if(pq.length < K){
                pq.insert(n.splitPoint);
            }
            //pop the point that is furthtest away if the new point is closer
            else if(distance(n.splitPoint, point) < distance(pq.front, point)){
                pq.insert(n.splitPoint);
                pq.popFront;
            }

            AABB!dim leftbbox = tempBox;
            //setting the max of the leftbox to the current splitpoint
            leftbbox.max[n.this_level] = n.splitPoint[n.this_level];

            AABB!dim rightbbox = tempBox;
            rightbbox.min[n.this_level] = n.splitPoint[n.this_level];

            if(n.leftChild !is null && (pq.length < K ||  
            distance(closest(leftbbox, point), point) < distance(pq.front, point)))
            {
                KnnRecurse(n.leftChild, leftbbox);
            }
            if(n.rightChild !is null && (pq.length < K || 
            distance(closest(rightbbox, point), point) < distance(pq.front, point)))
            {
                KnnRecurse(n.rightChild, rightbbox);
            }
        }
        KnnRecurse( root, aabb );
        return pq.release;
    }
}

// unittest{

//     auto points = [Point!2([0,0]), Point!2([1, 1]), Point!2([2,2])];
//     auto testKDTree = KDTree!2(points);
//     assert(testKDTree.rangeQuery(Point!2([2,3]), 5.0).length == 3);
// }

// unittest{
//     auto points = [Point!2([0,0]), Point!2([1, 1]), Point!2([2,2])];
//     auto testKDTree = KDTree!2(points);
//     auto returned = testKDTree.KNNQuery(Point!2([0.5, 0.5]), 2);
//     assert(returned.length == 2);
// }