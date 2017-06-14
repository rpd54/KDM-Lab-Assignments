

__author__ = 'RAKESH'


import sys
import nltk
nltk.data.path.append("/Users/USER/AppData/Roaming/nltk_data")
import QP,NER,who,when,what,how,time
import WM
import NET

#Global-lists
answer_list=[]
question_list=[]



stanford_stop_words_list=['a','an','and','are','as','at','be','buy','for','from',
                          'has','he','in','is','it','its','of','on','that','the',
                          'to','was','were','will','with']

NER_list=[]


start_time = time.time()

#!!!!!input file from the cmd !!!

if len(sys.argv) > 1:
    input_file=sys.argv[1]
else:
    print('Please provide an input file as an argument to this qa system !')

########!!!!! Reads input file #####!!!!!!!!!!!!!!!!!!!

with open(input_file, 'r') as input:
    data=input.readlines()

for i in range(0, len(data)):
    data[i]=data[i].replace("\n","")
    data[i]=data[i].replace(".story","")




directory_path=data[0]


for i in range(1, len(data)):
    story_id=data[i]
    question_path=directory_path + '/' + story_id + '.questions'
    story_path=directory_path + '/' + story_id + '.story'
    with open(story_path, 'r') as storyFile:
        story=storyFile.readlines()

    for i in range(0,len(story)):
        story[i]=story[i].replace("\n","")


    print('Story is :', story)
    sentences_list,hline_date=QP.story_parser(story)


    stops=set(stanford_stop_words_list)



    non_stop_words=[]
    stopwords_free_sentences_list=[]
    for sent in sentences_list:
        for w in sent.split():
            if w.lower() not in stops:
                non_stop_words.append(w)
        temp=' '.join(non_stop_words)
        stopwords_free_sentences_list.append(temp)
        non_stop_words=[]



    with open(question_path, 'r') as questionFile:
        question=questionFile.readlines()

    for i in range(0,len(question)):
        question[i]=question[i].replace("\n","")


    qIDList, qList,cleansedqList = QP.question_parser(question)

    non_stop_words=[]
    stopwords_free_questions_list=[]
    for sent in qList:
        for w in sent.split():
            if w.lower() not in stops:
                non_stop_words.append(w)
        temp=' '.join(non_stop_words)
        stopwords_free_questions_list.append(temp)
        non_stop_words=[]

    master_person_list=[]
    master_org_list=[]
    master_loc_list=[]
    master_month_list=[]
    master_time_list=[]
    master_money_list=[]
    master_percent_list=[]
    master_prof_list=[]

    for i in range(0, len(sentences_list)):
        temp_str=sentences_list[i]
        '''sentences_list[i]=sentences_list[i].strip()
        if sentences_list[i].index(',') != -1:
            if sentences_list[i][sentences_list[i].index(',')+1]!=' ':
                sentences_list[i]=sentences_list[i].replace(',','').replace('!','')'''
        temp_str=temp_str.strip()
        temp_str=temp_str.replace(',','').replace('!','')
        sent_person_list,sent_org_list,sent_loc_list,sent_month_list,sent_time_list,sent_money_list,sent_percent_list,sent_prof_list=NER.named_entity_recognition(temp_str)
        master_person_list.append(sent_person_list)
        master_org_list.append(sent_org_list)
        master_loc_list.append(sent_loc_list)
        master_month_list.append(sent_month_list)  #month and weekday names + season names
        master_time_list.append(sent_time_list)
        master_money_list.append(sent_money_list)
        master_percent_list.append(sent_percent_list)
        master_prof_list.append(sent_prof_list)


    who_list,what_list,when_list,why_list,where_list,how_list=[],[],[],[],[],[]


    for i in range(0, len(cleansedqList)):
        result=""
        qWords= cleansedqList[i].split()
        print(qList[i])
        q_flag=0
        question_list.append(qIDList[i])
        for j in range(0, len(qWords)):
            if qWords[j].lower()=='who' or qWords[j].lower()=='whom' or qWords[j].lower()=='whose'  :
                q_flag=1
                result=who.answering_who(cleansedqList[i],stopwords_free_questions_list[i],sentences_list,stopwords_free_sentences_list,master_person_list,master_prof_list) #stopwords_free_sentences_list
                break
            elif qWords[j].lower()=='what':
                q_flag=1
                result=what.answering_what(cleansedqList[i],stopwords_free_questions_list[i],sentences_list,stopwords_free_sentences_list,master_time_list,master_person_list) #stopwords_free_sentences_list
                break
            elif qWords[j].lower()=='when':
                q_flag=1
                result=when.answering_when(cleansedqList[i],stopwords_free_questions_list[i],sentences_list,stopwords_free_sentences_list,hline_date,master_month_list,master_time_list) #stopwords_free_sentences_list

                break
            elif qWords[j].lower()=='how':
                q_flag=1
                result=how.answering_how(cleansedqList[i],stopwords_free_questions_list[i],sentences_list,stopwords_free_sentences_list,master_time_list,master_percent_list) #stopwords_free_sentences_list
                break
            else:
                answer_list.append('No answer')
        if q_flag==0:
            print('Answer: No answer'+'\n')
        else:
            print('Answer: ', result +'\n')



