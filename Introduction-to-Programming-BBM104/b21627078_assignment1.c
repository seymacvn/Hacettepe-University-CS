#include <stdio.h>
#include <string.h>
#include<stdlib.h>
int main(int argc, char *argv[])
{
    struct files {
	int *number;
    char  *name;
    char *text;
   };
   typedef struct files files;
	char str[500],parametre1[100],seyma[100],*token,name[20],name1[20],name2[20],printw[20],prints[100];
    char splitStrings[500][100];
    int i,a,b,c,j,p,k,g,u,l,m,r,s,o,n=0,val,ebru,value,cnt,counter,len,com;
    FILE *file,*f;
    file = fopen(argv[1], "r");
    f = fopen("output.txt", "w");
    ebru=0;
 	while(!feof(file)) {
 	 	fscanf(file,"%s",parametre1);
        fgets(str,500,file);
        if(!strcmp(parametre1,"create"))
        ebru++;
		}
		files *sp = (files *) malloc(ebru*sizeof(files));
		rewind(file);
	 while(!feof(file)) {
 	 	fscanf(file,"%s",parametre1);
        fgets(str,500,file);
        len = strlen(str);
        if (len > 0 && str[len-1] == '\n') {
            str[--len] = '\0';
        }
    	fprintf(f,"%s %s\n",parametre1 ,str);
    	printf("%s %s\n",parametre1 ,str);
 	 	j=0; cnt=0;
    	for(i=0;i<=(strlen(str));i++)
    	{
        if(str[i]==' '||str[i]=='\0')
        {
            splitStrings[cnt][j]='\0';
            cnt++;
            j=0;
        }
        else
        {
            splitStrings[cnt][j]=str[i];
            j++;
        }
    }
    counter=0;
    for( p = 0; p < strlen(str); p++){

				if ( str[p] == ' ') counter++;
			}


    b = strcmp(parametre1,"create");
    k= strcmp(parametre1,"delete");
    l = strcmp(parametre1,"remove");
	m=strcmp(parametre1,"append");
	u=strcmp(parametre1,"replace");
	o=strcmp(parametre1,"print");


		if (b==0){
			b = strncmp(splitStrings[1],"-n",2);
			a =strncmp(splitStrings[1],"-t",2);
			g=strncmp(splitStrings[2],"-n",2);
			c = strncmp(splitStrings[3],"-t",2);
			u =strncmp(splitStrings[counter-1],"-n",2);
			if((((!b && !c) || (!a && !u) || (!g && !a))==0) || (counter<3)){
				fprintf(f,"Enter Enough Parameters!\n");
				printf("Enter Enough Parameters!\n");
			}
			else{
			n=n+1;
			b = strcmp(splitStrings[1],"-n");
			a=strcmp(splitStrings[1],"-t");
			if (b==0){
				if (counter==3){
					sp[n-1].number=malloc(1*sizeof*sp[n-1].number+1);
					*sp[n-1].number= n ;
					sp[n-1].name =malloc(strlen(splitStrings[2])*sizeof*sp[n-1].name+1);
					strcpy(sp[n-1].name,splitStrings[2]);
					sp[n-1].text=malloc(10*sizeof*sp[n-1].text+1);
					strcpy( sp[n-1].text,"Empty File" );
					}
				else{
					sp[n-1].number=malloc(1*sizeof*sp[n-1].number+1);
					*sp[n-1].number=n ;
					sp[n-1].name =malloc(strlen(splitStrings[2])*sizeof*sp[n-1].name+1);
					strcpy(sp[n-1].name,splitStrings[2]);
			   	    sp[n-1].name[strlen(sp[n-1].name)]='\0';
					c = strcmp(splitStrings[3],"-t");
					if(c==0){
						for(a=5; a<=counter;a++){
						strcat(strcat(splitStrings[4]," "), splitStrings[a]);
					     }
					    sp[n-1].text=malloc(strlen(splitStrings[4])*sizeof*sp[n-1].text+1);
						splitStrings[4][strlen(splitStrings[4])]='\0';
						strcpy( sp[n-1].text,splitStrings[4] );
						sp[n-1].text[strlen(sp[n-1].text)]='\0';
						}
					}
		     	}

			else if (a==0){
				a=strcmp(splitStrings[2],"-n");
				if(a==0){
					sp[n-1].number=malloc(1*sizeof*sp[n-1].number+1);
					*sp[n-1].number=n ;
					sp[n-1].name =malloc(strlen(splitStrings[3])*sizeof*sp[n-1].name+1);
					strcpy(sp[n-1].name,splitStrings[3]);
					splitStrings[3][strlen(splitStrings[3])]='\0';
					sp[n-1].name[strlen(sp[n-1].name)]='\0';
					sp[n-1].text=malloc(10*sizeof*sp[n-1].text+1);
					strcpy( sp[n-1].text,"Empty File" );
					sp[n-1].text[10]='\0';
					}
				else{
					sp[n-1].number=malloc(1*sizeof*sp[n-1].number+1);
					*sp[n-1].number=n ;
					for(a=3; a<=(counter-2);a++){
						strcat(strcat(splitStrings[2]," "), splitStrings[a]);
					     }
				    sp[n-1].text=malloc(strlen(splitStrings[2])*sizeof*sp[n-1].text+1);
					splitStrings[2][strlen(splitStrings[2])]='\0';
					strcpy( sp[n-1].text,splitStrings[2] );
					sp[n-1].text[strlen(sp[n-1].text)]='\0';
					sp[n-1].name =malloc(strlen(splitStrings[counter])*sizeof*sp[n-1].name+1);
					strcpy(sp[n-1].name,splitStrings[counter]);
					sp[n-1].name[strlen(sp[n-1].name)]='\0';

				}
			}
			}

		} 
		else if(k==0){
			for(a=0; a<n; a++){
				s=strncmp(splitStrings[2],sp[a].name,strlen(sp[a].name));
				if(s==0){
					sp[a].number[0]='\0';
					sp[a].name[0]='\0';
					sp[a].text[0]='\0';
				}
			}

		} 
		else if(l==0){
				for(a=0; a<n; a++){
				s=strncmp(splitStrings[2],sp[a].name,strlen(sp[a].name));
				if(sp[a].name[0]!= '\0'){
				if(s==0){
					val = atoi(splitStrings[4]);
					value=atoi(splitStrings[6]);
					if((val+value)>strlen(sp[a].text)){
				        sp[a].text = (char *) realloc(sp[a].text, val);
						for (c = val ; c < strlen(sp[a].text); c++){
						  sp[a].text[c] = sp[a].text[c+value];
						}
						sp[a].text[val]='\0';
					}
					else{
				    	sp[a].text = (char *) realloc(sp[a].text, strlen(sp[a].text));
						for (c = val ; c < strlen(sp[a].text)- value+1; c++){
						  sp[a].text[c] = sp[a].text[c+value];
						}
						sp[a].text[strlen(sp[a].text)]='\0';
					}
		    	 }
		   	   }
		   	    else{
		   			fprintf(f,"No Such File!\n");
		   			printf("No Such File!\n");
		   			break;
				  }
		    }
		} //remove bitti.
	else if (m==0){
    		b = strncmp(splitStrings[1],"-n",2);
			c=strncmp(splitStrings[1],"-t",2);
			a = strncmp(splitStrings[3],"-t",2);
			u =strncmp(splitStrings[counter-1],"-n",2);
			if(((!b && !a) || (!c && !u))==0){
				fprintf(f,"Please Enter The Appropriate Parameter\n");
				printf("Please Enter The Appropriate Parameter\n");
			}
			if(b==0){
				for(a=0; a<n; a++){
				b = strncmp(splitStrings[2],sp[a].name, strlen(splitStrings[2]));
				len=0;
				if(b==0){
					com=strncmp("Empty File",sp[a].text,10);
					if(com!=0){
				    b= strcmp(splitStrings[3],"-t");
				    if(b==0) {
    				    for(b=5; b<=counter;b++){
						 strcat(strcat(splitStrings[4]," "), splitStrings[b]);
						 }
						 splitStrings[4][strlen(splitStrings[4])]='\0';
				    	 sp[a].text = (char *) realloc(sp[a].text, strlen(sp[a].text)+strlen(splitStrings[4]));
						 strcat(sp[a].text,splitStrings[4]);
						 sp[a].text[strlen(sp[a].text)]='\0';
			            }
					   }
					if(com==0){
						for(k=0; k<strlen(sp[a].text); k++){
							sp[a].text[k]='\0';
						}
						b= strcmp(splitStrings[3],"-t");
				    	if(b==0) {
    				    for(b=5; b<=counter;b++){
    				  	 splitStrings[4][strlen(splitStrings[4])]='\0';
						 strcat(strcat(splitStrings[4]," "), splitStrings[b]);
						 }
						 splitStrings[4][strlen(splitStrings[4])]='\0';
				    	 sp[a].text = (char *) realloc(sp[a].text,strlen(splitStrings[4]));
						 strcat(sp[a].text,splitStrings[4]);
						 sp[a].text[strlen(sp[a].text)]='\0';
			            }	
					   }
				      }
					 
				    else{
				    	len++;
					}
				}
				if(len==n){
					fprintf(f,"No Such File!\n");
					printf("No Such File!\n");
					}
			}
		    else if(c==0){
		    	for(b=0; b<n; b++){
		    		len=0;
					s=strncmp(splitStrings[counter],sp[b].name,strlen(sp[b].name));
					if(s==0){
						for(a=3; a<=(counter-2);a++){
					    	strcat(strcat(splitStrings[2]," "), splitStrings[a]);
					     }
				    	splitStrings[2][strlen(splitStrings[2])]='\0';
	     				a= strlen(splitStrings[2])+strlen(sp[b].text);
				        sp[b].text = (char *) realloc(sp[b].text, strlen(sp[b].text)+strlen(splitStrings[2]));
						strcat(sp[b].text,splitStrings[2]);
						sp[b].text[strlen(sp[b].text)]='\0';
				 	}
				 	else{
				 		len++;
					 }
			    }
				if(len==n){
						fprintf(f,"No Such File!\n");
						printf("No Such File!\n");
					 }		
			}
	    
	} 
		else if(u==0){
		for(a=1; a<counter; a++){
			b=strcmp(splitStrings[a],"-n");
			c=strcmp(splitStrings[a],"-ow");
			u=strcmp(splitStrings[a],"-nw");
			if(b==0){
				s=a+1;
			}
			else if(c==0){
				r=a+1;
			}
			else if(u==0){
				p=a+1;
			}
		}

		for(a=0;a<n; a++){
					splitStrings[p][strlen(splitStrings[p])]='\0';
					splitStrings[r][strlen(splitStrings[r])]='\0';
					b=strcmp(splitStrings[s],sp[a].name);
					if(b==0){
						len=0;
						sp[a].text[strlen(sp[a].text)]='\0';
						for (s=0; s<strlen(sp[a].text); s++){

    					for (i = 0, j = 0; sp[a].text[i] != '\0' && splitStrings[r][j] != '\0'; i++)
    					{
        				if (sp[a].text[i] == splitStrings[r][j])
        				{
            				j++;
       					 }
       					else{
       						j=0;
						   }
  					   }
					}
					  if (j == strlen(splitStrings[r]))
  						  	c=i-j;
					        b=strlen(splitStrings[r]);
					        sp[a].text = (char *) realloc(sp[a].text, strlen(sp[a].text)- strlen(splitStrings[r]));
					        for (i = c; sp[a].text[i] != '\0'; i++){
					        		 sp[a].text[i]=sp[a].text[i+b];
								}
							for(u=0; u<c; u++) {
								seyma[u]=sp[a].text[u];
								if(u==(c-1)){
									seyma[u]=sp[a].text[u];
									seyma[c]='\0';
								}
								}
							seyma[strlen(seyma)]='\0';
							splitStrings[p][strlen(splitStrings[p])]='\0';
							strcat(seyma,splitStrings[p]);
							seyma[strlen(seyma)]='\0';
							for(u=c , b=strlen(seyma); u<=strlen(sp[a].text); u++,b++) {
									seyma[b]=sp[a].text[u];
								}
								seyma[strlen(seyma)]='\0';
						        sp[a].text = (char *) realloc(sp[a].text, strlen(seyma));
								strcpy(sp[a].text,seyma);
								sp[a].text[strlen(sp[a].text)]='\0';					  
						 	}
						}
			 			i = 0;
    							while(seyma[i] != '\0') {
        						seyma[i] = '\0';
        						i++;
								}
							}


	else if(o==0){
		b = strncmp(splitStrings[1],"-a",2);
		c=strncmp(splitStrings[1],"-e",2);
		u=strncmp(splitStrings[1],"-c",2);
		k=strncmp(splitStrings[3],"-t",2);
		l=strncmp(splitStrings[3],"-cw",3);
		m=strncmp(splitStrings[3],"-cs",3);

		if(b==0){
			for(a=0; a<n; a++){
  	 		if(sp[a].name[0]!= '\0'){
  			fprintf(f,"Filename %d : %s\n",a+1 ,sp[a].name);
  			printf("Filename %d : %s\n",a+1 ,sp[a].name);
  			}
		  }
		}
		else if(c==0){
			for(a=0; a<n; a++){
				if(sp[a].name[0]!= '\0'){
				strcpy(name1,sp[a].name);
				name1[strlen(name1)]='\0';
				token = strtok(name1,".");
				int  i=0;
    			while(token!=NULL){
    			if(i==0){
    				strcpy(name,token);
    				name[strlen(name)]='\0';
				}
				else if(i==1){
				token[strlen(token)]='\0';
				c=strncmp(splitStrings[2],token,strlen(token));
				if(c==0){
						name[strlen(name)]='\0';
						fprintf(f,"Filename %d: %s\n", a+1 ,name);
						printf("Filename %d: %s\n", a+1 ,name);
						name[strlen(name)]='\0';
  						fprintf(f,"Text: %s\n",sp[a].text);
  						printf("Text: %s\n",sp[a].text);
						}
					  }
						token=strtok(NULL,".\n");
         				i++;
					}
				}
					i = 0;
    				while(name1[i] != '\0') {
        				name1[i] = '\0';
        				i++;
					}
				}
			}
		else if (u==0){
			for(a=0; a<n; a++){
  	 		if(sp[a].name[0]!= '\0'){
  	 			strcpy(name2,sp[a].name);
  	 			token = strtok(name2,".");
					int  i=0;
    				while(token!=NULL){
        			if(i==0){
        			token[strlen(token)]='\0';
        		 	if(sp[a].name != '\0'){
        		 	fprintf(f,"Num: %d\n",*sp[a].number);
  					fprintf(f,"Name: %s\n",token);
  					fprintf(f,"Text: %s\n",sp[a].text);
  					printf("Num: %d\n",*sp[a].number);
  					printf("Name: %s\n",token);
  					printf("Text: %s\n",sp[a].text);
					}
					}
						token=strtok(NULL,".\n");
         				i++;
					}
	  			}
	  			i = 0;
    			while(name2[i] != '\0') {
        			name2[i] = '\0';
        			i++;
				}
	  			
			}
		}
		else if(k==0){
			for(a=0; a<n; a++){
				c=strncmp(splitStrings[2],sp[a].name,strlen(splitStrings[2]));
				if(c==0){
					if(sp[a].name[0]!= '\0'){
					fprintf(f,"Text: %s\n",sp[a].text);
					printf("Text: %s\n",sp[a].text);
				   }
				}
			}
		}
		else if(l==0){
			for(a=0; a<n; a++){
				c=strncmp(splitStrings[2],sp[a].name,strlen(splitStrings[2]));
			if(c==0){
					splitStrings[4][strlen(splitStrings[4])]='\0';
					strncpy(printw,splitStrings[4],strlen(splitStrings[4]));
					printw[strlen(printw)]='\0';
					strcpy(prints,sp[a].text);
					prints[strlen(prints)]='\0';
					val=strlen(printw);
					value=strlen(prints);
    				u = 0;
    				for(i=0; i <= value - val ; i++)
    				{

        			b = 1;
        			for(j=0; j<val; j++)
        			{
          			  if(prints[i + j] != printw[j])
          		   {
               		 b = 0;
               		 break;
           			}
       				 }

        			if(b == 1)
        			{
        		    u++;
        		}
    		}
    		if(sp[a].name[0]!= '\0'){
    			sp[a].name[strlen(sp[a].name)]='\0';
    			printw[strlen(printw)]='\0';
    			fprintf(f,"Text: %s\n",sp[a].text);
    			fprintf(f,"Number Of Occurrence of '%s' : %d\n",printw, u);
    			printf("Text: %s\n",sp[a].text);
    			printf("Number Of Occurrence of '%s' : %d\n",printw, u);
			}
    	 }
    	 	i = 0;
    		while(printw[i] != '\0' ) {
        	printw[i] = '\0';
        	i++;
			}
        	i = 0;
    		while(prints[i] != '\0' ) {
        	prints[i] = '\0';
        	i++;
   		 }
 	   }
	}	
		else if(m==0){
		for(a=0; a<n; a++){
				c=strncmp(splitStrings[2],sp[a].name,strlen(splitStrings[2]));
				if(c==0){
					u=0;
					for(b=0 ; b<strlen(sp[a].text) ; b++){
						if(sp[a].text[b]=='.' || sp[a].text[b]=='!' || sp[a].text[b]== '?')
						u++;
					}
					fprintf(f,"Number Of Sentences: %d\n",u);
					printf("Number Of Sentences: %d\n",u);
				}
			}
		}
	}
}
	for(a=0; a<ebru; a++){
		free(sp[a].number);
		free(sp[a].name);
		free(sp[a].text);
	}
	fclose(f);
	return 0;
}
