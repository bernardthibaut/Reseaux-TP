/*
    BERNARD Thibaut
    L3S6 Groupe 3
    Réseaux TP3
*/

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>


const char* ADDRESS = "244.0.0.1";
const short PORT = 7654;

int main(int argc, char const *argv[]) {
    struct sockaddr_in add;
    int s;

    add.sin_family = AF_INET;
    add.sin_port = PORT;
    add.sin_addr.s_addr = inet_addr(ADDRESS);

    s = socket(AF_INET, SOCK_STREAM, 0);
    if(s == -1){
        perror("Error socket");
        return EXIT_FAILURE;
    }

    return 0;
}
