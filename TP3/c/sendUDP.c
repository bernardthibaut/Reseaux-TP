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
#include <string.h>

const char* ADDRESS = "127.0.0.1";
const short PORT = 7654;

int main(int argc, char const *argv[]) {
    struct sockaddr_in add;
    int s;
    const char *buf = argv[1];

    if(argc != 2){
        perror("Wrong usage: sendUDP [msgToSend]");
        return EXIT_FAILURE;
    }

    add.sin_family = AF_INET;
    add.sin_port = htons(PORT);
    add.sin_addr.s_addr = inet_addr(ADDRESS);

    s = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if(s == -1){
        perror("Error socket");
        return EXIT_FAILURE;
    }

    if(sendto(s, buf, strlen(buf), 0, (struct sockaddr *) &add, sizeof(add))  < 0){
        perror("Error send");
        return EXIT_FAILURE;
    }

    return 0;
}
