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
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class Bot
{
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
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

        if (inlineQuery != null)
        {
            InlineQueryResultArticle Rock = buildInlineButton("rock", "Rock", "0");
            InlineQueryResultArticle Paper = buildInlineButton("paper", "Paper", "1");
            InlineQueryResultArticle Scissors = buildInlineButton("scissors", "Scissors", "2");
        } else if (message != null)
        {
            long chatId = message.chat().id();
            request = new SendMessage(chatId, "Hello!");
        }

        if (request != null)
        {
            bot.execute(request);
        }

    }

    private InlineQueryResultArticle buildInlineButton(String id, String title, String callbackData) {
        return new InlineQueryResultArticle(id, title, "I'm ready to fight!")
                .replyMarkup(
                        new InlineKeyboardMarkup(
                                new InlineKeyboardButton("Processing...").callbackData(callbackData)
                        )
                );
    }
}