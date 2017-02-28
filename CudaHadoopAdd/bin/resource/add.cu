extern "C"
__global__ void add(int n, float *hostInputA, float *hostInputB,float *result) {
     
     
	int blockId = blockIdx.x + blockIdx.y * gridDim.x + gridDim.x * gridDim.y * blockIdx.z;
	
	int i = blockId * (blockDim.x * blockDim.y * blockDim.z) + (threadIdx.z * (blockDim.x * blockDim.y)) + (threadIdx.y * blockDim.x) + threadIdx.x;
	  //  int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i<n) {
       result[i] = hostInputA[i] + hostInputB[i];
  }
}
