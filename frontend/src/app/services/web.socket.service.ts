import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs'; 
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private stompClient: Client;
  private messageSubject: Subject<any> = new Subject<any>();

  constructor() {
    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8080/topic/metrics',
      connectHeaders: {
        // Adicionando o token JWT ao cabeçalho
        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
      },
      onConnect: () => {
        console.log('STOMP Client Connected');
        // Inscreve-se no tópico para receber mensagens
        this.stompClient.subscribe('/topic/metrics', (message) => {
          this.messageSubject.next(message.body);  // Emite a mensagem recebida
        });
      },
      onStompError: (frame) => {
        console.error('STOMP Error:', frame);
      }
    });

    // Ativa a conexão STOMP
    this.stompClient.activate();
  }

  // Enviar mensagem para o servidor WebSocket
  sendMessage(message: string) {
    this.stompClient.publish({ destination: '/app/send-message', body: message });
  }

  // Retorna um Observable para se inscrever nas mensagens recebidas
  getMessages() {
    return this.messageSubject.asObservable();
  }

  // Fechar a conexão STOMP
  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}
