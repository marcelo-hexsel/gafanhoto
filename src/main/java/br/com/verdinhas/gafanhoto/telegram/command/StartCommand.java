package br.com.verdinhas.gafanhoto.telegram.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import br.com.verdinhas.gafanhoto.telegram.GafanhotoBot;
import br.com.verdinhas.gafanhoto.telegram.ReceivedMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartCommand implements BotCommand {

	@Override
	public String command() {
		return "/start";
	}

	@Override
	public void doIt(GafanhotoBot bot, ReceivedMessage message) {
		log.info("Executando comando start");

		sendConversation(message.chatId(), buildStartMessages(), bot);
	}

	private void sendConversation(Long chatId, List<String> messages, GafanhotoBot bot) {
		Runnable task = () -> {
			for (String message : messages) {
				bot.sendMessage(chatId, message);

				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			sendMonitorButton(bot, chatId);
		};

		new Thread(task).start();
	}

	private List<String> buildStartMessages() {
		List<String> messages = new ArrayList<>();

		messages.add("Olá jovem gafanhoto.");
		messages.add("Apartir de agora, vou poupar você de ficar frenéticamente atrás de promoções.");
		messages.add(
				"Posso te avisar quando novas promoções surgirem, para isso você pode me pedir para monitorar certas palavras-chave para você.");
		messages.add(
				"Para fazer isso digite o comando /monitorar e informe até 5 palavras-chave como por exemplo: TV Samsung LED");
		messages.add(
				"Escolha as palavras com cuidado, pois só vou te mostrar caso todas elas apareçam no link da promoção.");
		messages.add("Quando mais palavras, mais específica será a busca.");
		messages.add("Para listar todos os comandos disponíveis digite /help que te mostro.");

		return messages;
	}

	private void sendMonitorButton(GafanhotoBot bot, Long chatId) {
		SendMessage sendMessage = new SendMessage()
				.setText("O que acha de já adicionar seu primeiro monitor? Clique no botão abaixo.").setChatId(chatId);

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton().setText("monitorar")
				.setCallbackData("/monitorar");
		rowInline.add(inlineKeyboardButton);

		rowsInline.add(rowInline);

		markupInline.setKeyboard(rowsInline);
		sendMessage.setReplyMarkup(markupInline);

		bot.execute(sendMessage);
	}

}
