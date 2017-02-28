extern "C"
__global__ void feilei(int n, float *hostInputA, float *hostInputB,float *result) {

    int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i<n) {
       for(int j = 0; j < n; j++){
       if(hostInputA[j]==1.70141E38f){ //如果chang_tile[j/4]的值是无效值，则无用值赋为0      
          result[j] = hostInputA[j];
          continue;
       }
      if(hostInputA[j]>=4500.0f){
       result[j] = 1.0f;  //山地类型1
       } else if(hostInputA[j]>=3500.0f && hostInputA[j]<4500.0f){
          result[j]= 2.0f;   //山地类型2
       }  else if(hostInputA[j]>=2500.0f && hostInputA[j]<3500.0f){
          result[j]= 3.0f;   //山地类型3
       } else if(hostInputA[j]>=1500.0f && hostInputA[j]<2500.0f && hostInputB[j] >= 2.0f){
          result[j]= 4.0f;   //山地类型4
       }  else if(hostInputA[j]>=1500.0f && hostInputA[j]<1000.0f && hostInputB[j] >= 5.0f){
          result[j]= 5.0f;   //山地类型5
       } else if(hostInputA[j]>=300.0f && hostInputA[j]<1000.0f){
          result[j]= 6.0f;   //山地类型6
       } else{
         result[j] = 0.0f; //非山地
       }
    }
  }
}
