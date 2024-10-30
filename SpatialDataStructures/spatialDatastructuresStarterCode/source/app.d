import std.stdio;

import common;
import dumbknn;
import bucketknn;
import quadtree;
import kdtree;

//import your files here


    const size_t maxTestingTimesPerSingleTest = 10;
//---------------------------------------------PART 2 TIMING TESITNG-------------------------------------//

//VARYING ----------------------------------------------N -- k is 10, dimensions is 2---------------------------------------------------------
void varyingNGaussianKDTree(string fileName){
     File file = File(fileName, "w");
     file.writeln("N,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 1;
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        totalTime = 0;
        avgTime = 0;
        N = index* 10;
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getGaussianPoints!2(N);
            auto testingPoints = getGaussianPoints!2(numTestingPoints);
            auto kdTree = KDTree!2(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                kdTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(N, ",", avgTime);
    
     }
     writeln("closing file. csv made for varying N with Gaussian Distribution in KD Tree.");
    file.close();
}

void varyingNUniformKDTree(string fileName){
         File file = File(fileName, "w");
     file.writeln("N,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 1;
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        N = index* 10;
        totalTime = 0;
        avgTime = 0;

        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getUniformPoints!2(N);
            auto testingPoints = getUniformPoints!2(numTestingPoints);
            auto kdTree = KDTree!2(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                kdTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(N, "," , avgTime);
        
    }
     writeln("closing file. csv made for varying N with Uniform Distribution in KD Tree.");
    file.close();
}

void varyingNGaussianQuadTree(string fileName){
         File file = File(fileName, "w");
     file.writeln("N,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 1;
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        N = index* 10;
        totalTime = 0;
        avgTime = 0;

        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getGaussianPoints!2(N);
            auto testingPoints = getGaussianPoints!2(numTestingPoints);
            auto quadTree = quadTree(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                quadTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(N, "," , avgTime);
    }
     writeln("closing file. csv made for varying N with Gaussian Distribution for Quad Tree.");
    file.close();
}

void varyingNUniformQuadTree(string fileName){
         File file = File(fileName, "w");
     file.writeln("N,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 1;
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        N = index* 10;
        totalTime = 0;
        avgTime = 0;
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getUniformPoints!2(N);
            auto testingPoints = getUniformPoints!2(numTestingPoints);
            auto quadTree = quadTree(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                quadTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(N, "," , avgTime);
    }
     writeln("closing file. csv made for varying N with Uniform Distribution for Quad Tree.");
    file.close();
}


//VARYING k ---------------------------------------------------------- dimension is 2 , N is 250----------------------------------------
void varyingkGaussianKDTree(string fileName){
    File file = File(fileName, "w");
    file.writeln("k,avgTime");
    //declaring constants outside of the loop
    enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 250; //training points
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        k = index* 2;
        totalTime = 0;
        avgTime = 0;
      
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getGaussianPoints!2(N);
            auto testingPoints = getGaussianPoints!2(numTestingPoints);
            auto kdTree = KDTree!2(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                kdTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(k, ",",avgTime);
    }
     
    writeln("closing file. csv made for varying k with Gaussian Distribution for KD Tree.");
    file.close();
}

//VARYING k --- dimension is 2 , N is 250
void varyingkUniformKDTree(string fileName){
           File file = File(fileName, "w");
     file.writeln("k,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 250; //training points
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        k = index* 2;
        totalTime = 0;
        avgTime = 0;
  
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getUniformPoints!2(N);
            auto testingPoints = getUniformPoints!2(numTestingPoints);
            auto kdTree = KDTree!2(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                kdTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(k, "," , avgTime);
     }
     writeln("closing file. csv made for varying k with Uniform Distribution for KD Tree.");
    file.close();
}

void varyingkGaussianQuadtree(string fileName){
           File file = File(fileName, "w");
     file.writeln("k,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 250; //training points
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        k = index* 2;
        totalTime = 0;
        avgTime = 0;
  
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getGaussianPoints!2(N);
            auto testingPoints = getGaussianPoints!2(numTestingPoints);
            auto quadTree = quadTree(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                quadTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(k, "," , avgTime);
     }
     writeln("closing file. csv made for varying k with Gaussian Distribution for Quad Tree.");
    file.close();
}

void varyingkUniformQuadTree(string fileName){
           File file = File(fileName, "w");
     file.writeln("k,avgTime");
     //declaring constants outside of the loop
     enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 250; //training points
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        k = index* 2;
        totalTime = 0;
        avgTime = 0;
  
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getUniformPoints!2(N);
            auto testingPoints = getUniformPoints!2(numTestingPoints);
            auto quadTree = quadTree(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                quadTree.KNNQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(k, "," , avgTime);
     }
     writeln("closing file. csv made for varying k with Uniform Distribution for Quad Tree.");
    file.close();
}

//varying -----------------------------------------------D for kd trees-----------------------------------------------------------
void varyingDGaussian(string fileName){
    File file = File(fileName, "w");
    file.writeln("dimension,avgTime");
    enum N = 250;
    enum numTestingPoints = 50;
    auto k = 10;
    long totalTime = 0;
    long avgTime = 0;
    static foreach(dimension; 1 .. 10){
        totalTime = 0;
        avgTime = 0;
    
        foreach(testID; 1 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getGaussianPoints!dimension(N);
            auto testingPoints = getGaussianPoints!dimension(numTestingPoints);
            auto kdTree = KDTree!dimension(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref x ; testingPoints){
                kdTree.KNNQuery(x, 10);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / (maxTestingTimesPerSingleTest);
        file.writeln(dimension, "," , avgTime);
    }
    writeln("closing file. csv made for varying D with Gaussian Distribution for KD Tree.");
    file.close();
}

void varyingDUniform(string fileName){
    File file = File(fileName, "w");
    file.writeln("dimension,avgTime");
    enum N = 250;
    enum numTestingPoints = 50;
    auto k = 10;
    long totalTime = 0;
    long avgTime = 0;
    static foreach(dimension; 1 .. 10){
        totalTime = 0;
        avgTime = 0;
        foreach(testID; 1 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getUniformPoints!dimension(N);
            auto testingPoints = getUniformPoints!dimension(numTestingPoints);
            auto kdTree = KDTree!dimension(trainingPoints);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref x ; testingPoints){
                kdTree.KNNQuery(x, 10);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        }
        avgTime = totalTime / (maxTestingTimesPerSingleTest);
        file.writeln(dimension, "," , avgTime);
    }
    writeln("closing file. csv made for varying D with Uniform Distribution for KD Tree.");
    file.close();
}

//varying ------------------------------------------------------k and N -----------------------------------------------------------
void varyingKAndNUniformQuad(string fileName){
    File file = File(fileName, "w");
    file.writeln("N,k,avgTime");

    enum int[] kValues = [1,5,10,20,50,100];
    enum int[] nValues = [100, 500, 1000, 5000, 10000, 100000];
    enum numTestingPoints = 50;
    long totalTime = 0;
    long avgTime = 0;

    foreach(N; nValues){
        foreach(k ; kValues){
            totalTime = 0;
            avgTime = 0;
            foreach(testId; 0 .. maxTestingTimesPerSingleTest){
                auto trainingPoints = getUniformPoints!2(N);
                auto testingPoints = getUniformPoints!2(numTestingPoints);
                auto quadTree = quadTree(trainingPoints);
                auto stopWatch = StopWatch(AutoStart.no);
                stopWatch.start;
                foreach(const ref p ; testingPoints){
                    quadTree.KNNQuery(p,k);
                }
                stopWatch.stop;
                totalTime += stopWatch.peek.total!"usecs";
            }
            avgTime = totalTime / maxTestingTimesPerSingleTest;
            file.writeln(N,",",k,",",avgTime);
        }
    }
    writeln("closing file. csv made for varying k and N with Uniform Distribution for Quad Tree.");
    file.close();
}

void varyingKAndNUniformKD(string fileName){
    File file = File(fileName, "w");
    file.writeln("N,k,avgTime");

    enum int[] kValues = [1,5,10,20,50,100];
    enum int[] nValues = [100, 500, 1000, 5000, 10000, 100000];
    enum numTestingPoints = 50;
    long totalTime = 0;
    long avgTime = 0;

    foreach(N; nValues){
        foreach(k ; kValues){
            totalTime = 0;
            avgTime = 0;
            foreach(testId; 0 .. maxTestingTimesPerSingleTest){
                auto trainingPoints = getUniformPoints!2(N);
                auto testingPoints = getUniformPoints!2(numTestingPoints);
                auto kdTree = KDTree!2(trainingPoints);
                auto stopWatch = StopWatch(AutoStart.no);
                stopWatch.start;
                foreach(const ref p ; testingPoints){
                    kdTree.KNNQuery(p,k);
                }
                stopWatch.stop;
                totalTime += stopWatch.peek.total!"usecs";
            }
            avgTime = totalTime / maxTestingTimesPerSingleTest;
            file.writeln(N,",",k,",",avgTime);
        }
    }
    writeln("closing file. csv made for varying k and N with Uniform Distribution for KD Tree.");
    file.close();
}

void varyingKAndNGaussianQuad(string fileName){
    File file = File(fileName, "w");
    file.writeln("N,k,avgTime");

    enum int[] kValues = [1,5,10,20,50,100];
    enum int[] nValues = [100, 500, 1000, 5000, 10000, 100000];
    enum numTestingPoints = 50;
    long totalTime = 0;
    long avgTime = 0;

    foreach(N; nValues){
        foreach(k ; kValues){
            totalTime = 0;
            avgTime = 0;
            foreach(testId; 0 .. maxTestingTimesPerSingleTest){
                auto trainingPoints = getGaussianPoints!2(N);
                auto testingPoints = getGaussianPoints!2(numTestingPoints);
                auto quadTree = quadTree(trainingPoints);
                auto stopWatch = StopWatch(AutoStart.no);
                stopWatch.start;
                foreach(const ref p ; testingPoints){
                    quadTree.KNNQuery(p,k);
                }
                stopWatch.stop;
                totalTime += stopWatch.peek.total!"usecs";
            }
            avgTime = totalTime / maxTestingTimesPerSingleTest;
            file.writeln(N,",",k,",",avgTime);
        }
    }
    writeln("closing file. csv made for varying k and N with Gaussian Distribution for Quad Tree.");
    file.close();
}


void varyingKAndNGaussianKD(string fileName){
    File file = File(fileName, "w");
    file.writeln("N,k,avgTime");

    enum int[] kValues = [1,5,10,20,50,100];
    enum int[] nValues = [100, 500, 1000, 5000, 10000, 100000];
    enum numTestingPoints = 50;
    long totalTime = 0;
    long avgTime = 0;

    foreach(N; nValues){
        foreach(k ; kValues){
            totalTime = 0;
            avgTime = 0;
            foreach(testId; 0 .. maxTestingTimesPerSingleTest){
                auto trainingPoints = getGaussianPoints!2(N);
                auto testingPoints = getGaussianPoints!2(numTestingPoints);
                auto kdTree = KDTree!2(trainingPoints);
                auto stopWatch = StopWatch(AutoStart.no);
                stopWatch.start;
                foreach(const ref p ; testingPoints){
                    kdTree.KNNQuery(p,k);
                }
                stopWatch.stop;
                totalTime += stopWatch.peek.total!"usecs";
            }
            avgTime = totalTime / maxTestingTimesPerSingleTest;
            file.writeln(N,",",k,",",avgTime);
        }
    }
    writeln("closing file. csv made for varying k and N with Gaussian Distribution for KD Tree.");
    file.close();
}


//------------------------------------------------ bucket testing --------------------------------------------------------
void bucketkTiming(string fileName){
    File file = File(fileName, "w");
    file.writeln("");
    enum numTestingPoints = 50;
    auto k = 10;
    size_t N = 1; //training points
    long totalTime = 0;
    long avgTime = 0;

     static foreach(index; 1 .. 50){
        //setting the varying N for this test
        N = index* 10;
        totalTime = 0;
        avgTime = 0;
  
        foreach(testId; 0 .. maxTestingTimesPerSingleTest){
            auto trainingPoints = getUniformPoints!2(N);
            auto testingPoints = getUniformPoints!2(numTestingPoints);
            auto bucket = BucketKNN!2(trainingPoints, 2);
            auto stopWatch = StopWatch(AutoStart.no);
            stopWatch.start;
            foreach(const ref p ; testingPoints){
                bucket.knnQuery(p,k);
            }
            stopWatch.stop;
            totalTime += stopWatch.peek.total!"usecs";
        
        }
        avgTime = totalTime / maxTestingTimesPerSingleTest;
        file.writeln(N, "," , avgTime);
     }
     writeln("closing file. csv made for bucket Tree.");
    file.close();
}

void main()
{
    //varying N KD Tree--
    // varyingNGaussianKDTree("varyingNGaussianKDTree.csv");
    // varyingNUniformKDTree("varyingNUniformKDTree.csv");
    // //varying N Quad Tree --
    // varyingNGaussianQuadTree("varyingNGaussianQuadTree.csv");
    // varyingNUniformQuadTree("varyingNUniformQuadTree.csv");

    // //varying k KD Tree --
    // varyingkGaussianKDTree("varyingkGaussianKDTree.csv");
    // varyingkUniformKDTree("varyingkUniformKDTree.csv");
    // //varying k Quad Tree --
    // varyingkGaussianQuadtree("varyingkGaussianQuadTree.csv");
    // varyingkUniformQuadTree("varyingkUniformQuadTree.csv");

    // //varying D KD Tree -- 
    // varyingDGaussian("varyingDGaussian.csv");
    // varyingDUniform("varyingDUniform.csv");

    // //varying k and N KD Tree --
    // varyingKAndNGaussianKD("varyingKNGaussianKDTree.csv");
    // varyingKAndNUniformKD("varyingKNUniformKDTree.csv");

    // //varying k and N Quad Tree --
    // varyingKAndNGaussianQuad("varyingKNGaussianQuadTree.csv");
    // varyingKAndNUniformQuad("varyingKNUniformQuadTree.csv");
    

    //bucket file
    //expected to perform better with uniform and not gaussian
    //bucketkGaussianTiming("varyingkGaussianBucket.csv");
   // bucketkTiming("varyingNUGaussianBucket.csv");










    
}

    //because dim is a "compile time parameter" we have to use "static foreach"
    //to loop through all the dimensions we want to test.
    //the {{ are necessary because this block basically gets copy/pasted with
    //dim filled in with 1, 2, 3, ... 7.  The second set of { lets us reuse
    //variable names.
    // writeln("dumbKNN results");
    // static foreach(dim; 1..8){{
    //     //get points of the appropriate dimension
    //     auto trainingPoints = getGaussianPoints!dim(1000);
    //     auto testingPoints = getUniformPoints!dim(100);
    //     auto kd = DumbKNN!dim(trainingPoints);
    //     writeln("tree of dimension ", dim, " built");
    //     auto sw = StopWatch(AutoStart.no);
    //     sw.start; //start my stopwatch
    //     foreach(const ref qp; testingPoints){
    //         kd.knnQuery(qp, 10);
    //     }
    //     sw.stop;
    //     writeln(dim, sw.peek.total!"usecs"); //output the time elapsed in microseconds
    //     //NOTE, I SOMETIMES GOT TOTALLY BOGUS TIMES WHEN TESTING WITH DMD
    //     //WHEN YOU TEST WITH LDC, YOU SHOULD GET ACCURATE TIMING INFO...
    // }}

    // writeln("BucketKNN results");
    // //Same tests for the BucketKNN
    // static foreach(dim; 1..8){{
    //     //get points of the appropriate dimension
    //     enum numTrainingPoints = 1000;
    //     auto trainingPoints = getGaussianPoints!dim(numTrainingPoints);
    //     auto testingPoints = getUniformPoints!dim(100);
    //     auto kd = BucketKNN!dim(trainingPoints, cast(int)pow(numTrainingPoints/64, 1.0/dim)); //rough estimate to get 64 points per cell on average
    //     writeln("tree of dimension ", dim, " built");
    //     auto sw = StopWatch(AutoStart.no);
    //     sw.start; //start my stopwatch
    //     foreach(const ref qp; testingPoints){
    //         kd.knnQuery(qp, 10);
    //     }
    //     sw.stop;
    //     writeln(dim, sw.peek.total!"usecs"); //output the time elapsed in microseconds
    //     //NOTE, I SOMETIMES GOT TOTALLY BOGUS TIMES WHEN TESTING WITH DMD
    //     //WHEN YOU TEST WITH LDC, YOU SHOULD GET ACCURATE TIMING INFO...
    // }}

    // writeln("QuadTree results");
    // static foreach(dim; 1..8){{
    //     enum numTrainingPoitns = 1000;
    //     auto trianingPoints = getGaussianPoints!dim(numTrainingPoints);
    //     auto testingPints = getUniformPoints!dim(100);
    // }}
