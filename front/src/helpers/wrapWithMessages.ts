import { message } from "antd";

export function wrapWithMessages (action: () => Promise<any>, actionOnSuccess: () => void = null,
loadingText: string = 'В процессе...', successText: string = 'Готово!', errorText: string = 'Ошибка!',
 delay: number = 2.5) { 
    
    function wrappedAction(){
        message.loading(loadingText);
        action().then( () => {
            if(actionOnSuccess) actionOnSuccess();
            message.destroy();    
            message.success(successText, delay);
        })
        .catch((err) => {
            message.destroy();
            console.log(err);
            message.error(errorText, delay);
        });       
    }
    return wrappedAction;
}