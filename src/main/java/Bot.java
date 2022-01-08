import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class Bot
{
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    private final String PROCESSING_LABEL = "Processing...";

    public void serve() // получения списка обновлений от telegram API (обновления содержат в себе действия со стороны пользователя)
    {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process); // по штучное обновление метода process
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update)
    {
        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        InlineQuery inlineQuery = update.inlineQuery();

        BaseRequest request = null;

        if (message != null && message.viaBot() != null && message.viaBot().username().equals("RPSnewgame_bot"))
        {
            InlineKeyboardMarkup replyMarkup = message.replyMarkup();
            if (replyMarkup == null)
            {
                return;
            }

            InlineKeyboardButton[][] buttons = replyMarkup.inlineKeyboard();

            if (buttons == null)
            {
                return;
            }
            String senderChose = buttons[0][0].text();

            if (!senderChose.equals(PROCESSING_LABEL))
            {
                return;
            }
        } else if (inlineQuery != null)
        {
            InlineQueryResultArticle rock = buildInlineButton("rock", "\uD83D\uDC4A Rock", "0");
            InlineQueryResultArticle paper = buildInlineButton("paper", "✋ Paper", "1");
            InlineQueryResultArticle scissors = buildInlineButton("scissors", "✌ Scissors", "2");

            request = new AnswerInlineQuery(inlineQuery.id(), rock, paper, scissors);

        } /*else if (message != null)
        {
            long chatId = message.chat().id();
            request = new SendMessage(chatId, "Hello!");
        }*/

        if (request != null)
        {
            bot.execute(request);
        }

    }

    private InlineQueryResultArticle buildInlineButton(String id, String title, String callbackData) {
        return new InlineQueryResultArticle(id, title, "I'm ready to fight!")
                .replyMarkup(
                        new InlineKeyboardMarkup(
                                new InlineKeyboardButton(PROCESSING_LABEL).callbackData(callbackData)
                        )
                );
    }
}