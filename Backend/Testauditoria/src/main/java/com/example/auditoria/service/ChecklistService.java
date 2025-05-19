package com.example.auditoria.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auditoria.dto.ChecklistItem;
import com.example.auditoria.entity.Checklist;
import com.example.auditoria.repository.ChecklistRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private AIRecommendationService aiRecommendationService;
    
    public ChecklistRepository getChecklistRepository() {
        return checklistRepository;
    }
    
    // Obtém todos os controlos ordenados
    public List<Checklist> getAllItems() {
        return checklistRepository.findAllByOrderByDisplayorderAsc();
    }
    
    // Obtém um controlo específico por ID
    public Checklist getItem(Long id) {
        return checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Controlo da Checklist não encontrado : " + id));
    }

    @Transactional
    public Checklist updateItem(Long id, ChecklistItem itemDTO) {
        Checklist item = getItem(id);

        // Evita atualizar itens de cabeçalho
        if (item.getIsHeader()) {
            return item;
        }

        // Atualiza os campos com os valores do DTO
        if (itemDTO.getEmConformidade() != null) {
            // Verifica se o estado de conformidade foi alterado
            boolean statusChanged = (item.getEmConformidade() == null || 
                                   !item.getEmConformidade().equals(itemDTO.getEmConformidade()));
            
            item.setEmConformidade(itemDTO.getEmConformidade());
            
            // Se o estado de conformidade foi alterado, cria uma recomendação
            if (statusChanged) {
                String recommendation = aiRecommendationService.generateRecommendation(
                    item.getControlos(),
                    item.getTarefas(),
                    itemDTO.getEmConformidade()
                );
                
                item.setAiNotas(recommendation);
                System.out.println("Recomendação gerada: " + recommendation);
            }
        }
        
        if (itemDTO.getNotas() != null) {
            item.setNotas(itemDTO.getNotas());
        }

        // Guardar e retornar o controlo atualizado
        Checklist savedItem = checklistRepository.save(item);
        System.out.println("Cotnrolo guardado: " + savedItem.getId() + ", AI Notas: " + savedItem.getAiNotas());
        return savedItem;
    }
    
    // Atualiza múltiplos controlos
    @Transactional
    public List<Checklist> updateBulkItems(Map<Long, Boolean> complianceUpdates) {
        List<Checklist> updatedItems = new ArrayList<>();

        for (Map.Entry<Long, Boolean> entry : complianceUpdates.entrySet()) {
            Long itemId = entry.getKey();
            Boolean emConformidade = entry.getValue();

            Checklist item = getItem(itemId);
            if (item.getIsHeader()) continue; // Pula itens de cabeçalho
            
            boolean statusChanged = (item.getEmConformidade() == null || 
                                   !item.getEmConformidade().equals(emConformidade));
            
            item.setEmConformidade(emConformidade);

            // Só cria recomendação se o estado de conformidade mudou
            if (statusChanged) {
                String recommendation = aiRecommendationService.generateRecommendation(
                    item.getControlos(),
                    item.getTarefas(),
                    emConformidade
                );
                item.setAiNotas(recommendation);
            }

            updatedItems.add(checklistRepository.save(item));
        }

        return updatedItems;
    }
    
    // Inicializa a checklist com controlos padrões da ISO 27001
    @PostConstruct
    public void initializeChecklistItems() {
        if (checklistRepository.count() == 0) {
            List<Checklist> allItems = new ArrayList<>();
            int order = 1;

            // 5 - Políticas de segurança da informação
            addHeader(allItems, "5 - Políticas de segurança da informação", order++);
            addControlItem(allItems, "5.1", "Políticas para segurança da informação", "Existe um conjunto documentado de políticas de segurança da informação aprovado pela gestão?", order++);
            addControlItem(allItems, "5.2", "Revisão das políticas", "As políticas de segurança da informação são revistas em intervalos planeados ou quando ocorrem mudanças significativas?", order++);
            addControlItem(allItems, "5.3", "Papéis e responsabilidades", "Os papéis e responsabilidades de segurança da informação estão claramente definidos e comunicados?", order++);
            addControlItem(allItems, "5.4", "Procedimento para autorização", "Existem procedimentos formais para autorização de instalações de processamento de informação?", order++);
            addControlItem(allItems, "5.5", "Contactos com autoridades", "São mantidos contactos apropriados com autoridades relevantes?", order++);
            addControlItem(allItems, "5.6", "Contactos com grupos de interesse", "São mantidos contactos com grupos de interesse especial ou outros fóruns de segurança e associações profissionais?", order++);
            addControlItem(allItems, "5.7", "Gestão de projetos", "A segurança da informação é abordada na gestão de projetos?", order++);
            addControlItem(allItems, "5.8", "Inventário de informações", "Existe um inventário atualizado dos ativos de informação?", order++);
            addControlItem(allItems, "5.9", "Uso aceitável dos ativos", "Existem regras documentadas para o uso aceitável de informações e ativos?", order++);
            addControlItem(allItems, "5.10", "Devolução dos ativos", "Existe um processo para garantir que os funcionários e terceiros devolvam todos os ativos organizacionais após o término do emprego ou contrato?", order++);
            addControlItem(allItems, "5.11", "Classificação da informação", "A informação é classificada em termos de requisitos legais, valor, criticidade e sensibilidade?", order++);
            addControlItem(allItems, "5.12", "Rotulagem da informação", "Existe um conjunto apropriado de procedimentos para rotulagem da informação de acordo com o esquema de classificação adotado?", order++);
            addControlItem(allItems, "5.13", "Transferência de informação", "Existem procedimentos, controlos e acordos formais para a transferência de informações para entidades externas?", order++);
            addControlItem(allItems, "5.14", "Identificação dos requisitos legais", "Todos os requisitos legislativos, regulamentares e contratuais relevantes estão identificados e documentados?", order++);
            addControlItem(allItems, "5.15", "Direitos de propriedade intelectual", "São implementados procedimentos para garantir o cumprimento dos direitos de propriedade intelectual?", order++);
            addControlItem(allItems, "5.16", "Proteção e privacidade de dados pessoais", "A privacidade e a proteção de dados pessoais são asseguradas conforme exigido pela legislação aplicável?", order++);
            addControlItem(allItems, "5.17", "Revisão independente da segurança da informação", "A abordagem da organização para gerir a segurança da informação é revista independentemente em intervalos planeados?", order++);
            addControlItem(allItems, "5.18", "Identificação de riscos relacionados com partes externas", "Os riscos para a informação da organização associados a partes externas são identificados e tratados?", order++);
            addControlItem(allItems, "5.19", "Abordagem de segurança na relação com fornecedores", "Existe um procedimento estabelecido para gerir a segurança da informação na cadeia de fornecimento?", order++);
            addControlItem(allItems, "5.20", "Tratamento da segurança em acordos com fornecedores", "Os acordos com fornecedores incluem requisitos para tratar os riscos de segurança da informação?", order++);
            addControlItem(allItems, "5.21", "Gestão de serviços de fornecedores", "Os serviços, relatórios e registros fornecidos por terceiros são regularmente monitorados, revistos e auditados?", order++);
            addControlItem(allItems, "5.22", "Gestão de mudanças nos serviços de fornecedores", "As mudanças nos serviços prestados por fornecedores são geridas, considerando a criticidade da informação envolvida?", order++);
            addControlItem(allItems, "5.23", "Segurança da informação na continuidade", "A continuidade da segurança da informação está incorporada nos sistemas de gestão de continuidade de negócio?", order++);
            addControlItem(allItems, "5.24", "Planeamento da continuidade da segurança da informação", "A organização determinou seus requisitos para a continuidade da segurança da informação em situações adversas?", order++);
            addControlItem(allItems, "5.25", "Implementação da continuidade da segurança da informação", "A organização estabeleceu, documentou, implementou e mantém processos, procedimentos e controlos para assegurar a continuidade da segurança da informação?", order++);
            addControlItem(allItems, "5.26", "Verificação, revisão e avaliação da continuidade", "A organização verifica os controlos de continuidade de segurança da informação em intervalos regulares?", order++);
            addControlItem(allItems, "5.27", "Redundâncias", "As instalações de processamento de informação são implementadas com redundância suficiente para atender aos requisitos de disponibilidade?", order++);

            // 6 - Organização da segurança da informação
            addHeader(allItems, "6 - Organização da segurança da informação", order++);
            addControlItem(allItems, "6.1", "Responsabilidades e papéis", "As responsabilidades pela segurança da informação são claramente definidas e atribuídas?", order++);
            addControlItem(allItems, "6.2", "Segregação de funções", "As tarefas e áreas de responsabilidade são segregadas para reduzir oportunidades de modificação não autorizada ou uso indevido de ativos?", order++);
            addControlItem(allItems, "6.3", "Contacto com autoridades", "São mantidos contactos apropriados com autoridades relevantes?", order++);
            addControlItem(allItems, "6.4", "Contactos com grupos de interesse", "São mantidos contactos apropriados com grupos de interesse especial ou outros fóruns e associações profissionais?", order++);
            addControlItem(allItems, "6.5", "Segurança da informação na gestão de projetos", "A segurança da informação é abordada na gestão de projetos, independentemente do tipo de projeto?", order++);
            addControlItem(allItems, "6.6", "Política para dispositivos móveis", "Uma política e medidas de suporte são adotadas para gerenciar os riscos da utilização de dispositivos móveis?", order++);
            addControlItem(allItems, "6.7", "Teletrabalho", "Uma política e medidas de apoio são implementadas para proteger as informações acessadas, processadas ou armazenadas em locais de teletrabalho?", order++);

            // 7 - Segurança relativa às pessoas
            addHeader(allItems, "7 - Segurança relativa às pessoas", order++);
            addControlItem(allItems, "7.1", "Rastreio", "Verificações em segundo plano de todos os candidatos a emprego são realizadas de acordo com leis e regulamentos relevantes?", order++);
            addControlItem(allItems, "7.2", "Termos e condições de emprego", "Os acordos contratuais com funcionários e contratados declaram suas responsabilidades pela segurança da informação?", order++);
            addControlItem(allItems, "7.3", "Conscientização, educação e formação", "Os funcionários da organização recebem treinamento e atualizações regulares sobre políticas e procedimentos de segurança da informação?", order++);
            addControlItem(allItems, "7.4", "Processo disciplinar", "Existe um processo disciplinar formal para lidar com violações de segurança da informação?", order++);
            addControlItem(allItems, "7.5", "Responsabilidades no término ou mudança de emprego", "As responsabilidades pela segurança da informação após o término ou mudança de emprego são comunicadas e aplicadas?", order++);
            addControlItem(allItems, "7.6", "Acordos de confidencialidade", "Acordos de confidencialidade são identificados, documentados e regularmente revisados?", order++);
            addControlItem(allItems, "7.7", "Trabalho remoto", "Existem políticas e medidas de segurança para proteger informações acessadas, processadas ou armazenadas em locais de trabalho remoto?", order++);
            addControlItem(allItems, "7.8", "Conscientização sobre engenharia social e ameaças de phishing", "Os funcionários são conscientizados e treinados sobre técnicas de engenharia social e como reconhecer ataques de phishing?", order++);

            // 8 - Gestão de ativos
            addHeader(allItems, "8 - Gestão de ativos", order++);
            addControlItem(allItems, "8.1", "Inventário dos ativos", "Os ativos associados à informação e instalações de processamento de informação são identificados e mantidos num inventário?", order++);
            addControlItem(allItems, "8.2", "Atribuição de ativos", "Os ativos mantidos no inventário têm proprietários formalmente designados?", order++);
            addControlItem(allItems, "8.3", "Uso aceitável dos ativos", "Regras para o uso aceitável de informações e ativos são identificadas, documentadas e implementadas?", order++);
            addControlItem(allItems, "8.4", "Devolução de ativos", "Todos os funcionários e utilizadores de partes externas devolvem todos os ativos organizacionais em sua posse após o término?", order++);
            addControlItem(allItems, "8.5", "Classificação da informação", "A informação é classificada de acordo com as necessidades de segurança da organização?", order++);
            addControlItem(allItems, "8.6", "Rotulagem da informação", "A informação é rotulada de acordo com o esquema de classificação de informação adotado pela organização?", order++);
            addControlItem(allItems, "8.7", "Manuseamento da informação", "Existem procedimentos para o manuseamento de ativos de acordo com o esquema de classificação de informação?", order++);
            addControlItem(allItems, "8.8", "Gestão de suportes de armazenamento", "São implementados procedimentos para gestão de suportes removíveis de acordo com o esquema de classificação?", order++);
            addControlItem(allItems, "8.9", "Eliminação de suportes de armazenamento", "Os suportes de armazenamento são descartados de forma segura quando não são mais necessários?", order++);
            addControlItem(allItems, "8.10", "Transferência física de suportes", "Os suportes contendo informações são protegidos contra acesso não autorizado, uso indevido ou corrupção durante o transporte?", order++);
            addControlItem(allItems, "8.11", "Utilizadores com identidade privilegiada", "A alocação e uso de direitos de acesso privilegiado são restritos e controlados?", order++);
            addControlItem(allItems, "8.12", "Gestão de informação de autenticação secreta", "A alocação de informação de autenticação secreta é controlada através de um processo de gestão formal?", order++);

            // 9 - Controlo de acessos
            addHeader(allItems, "9 - Controlo de acessos", order++);
            addControlItem(allItems, "9.1", "Política de controlo de acesso", "Uma política de controlo de acesso é estabelecida, documentada e revista com base nos requisitos de negócio e de segurança?", order++);
            addControlItem(allItems, "9.2", "Acesso a redes e serviços de rede", "Os utilizadores só têm acesso à rede e serviços de rede específicos que foram explicitamente autorizados a utilizar?", order++);
            addControlItem(allItems, "9.3", "Registo e cancelamento de utilizadores", "Um processo formal de registo e cancelamento de utilizadores está implementado para permitir a atribuição de direitos de acesso?", order++);
            addControlItem(allItems, "9.4", "Gestão de direitos de acesso privilegiado", "A atribuição e utilização de direitos de acesso privilegiado é restrita e controlada?", order++);
            addControlItem(allItems, "9.5", "Gestão de informação secreta de autenticação", "A atribuição de informação secreta de autenticação é controlada através de um processo de gestão formal?", order++);
            addControlItem(allItems, "9.6", "Revisão dos direitos de acesso", "Os proprietários dos ativos revêem os direitos de acesso dos utilizadores em intervalos regulares?", order++);
            addControlItem(allItems, "9.7", "Remoção ou ajuste de direitos de acesso", "Os direitos de acesso de todos os funcionários e utilizadores externos são removidos após o término do emprego, contrato ou acordo?", order++);
            addControlItem(allItems, "9.8", "Uso de informação secreta de autenticação", "Os utilizadores seguem as práticas da organização no uso de informação secreta de autenticação?", order++);
            addControlItem(allItems, "9.9", "Restrição de acesso à informação", "O acesso à informação e funções de aplicações é restrito de acordo com a política de controlo de acesso?", order++);
            addControlItem(allItems, "9.10", "Procedimentos seguros de início de sessão", "Quando requerido pela política de controlo de acesso, o acesso a sistemas e aplicações é controlado por um procedimento seguro de início de sessão?", order++);
            addControlItem(allItems, "9.11", "Sistema de gestão de palavras-passe", "Os sistemas de gestão de palavras-passe são interativos e garantem palavras-passe de qualidade?", order++);
            addControlItem(allItems, "9.12", "Utilização de programas utilitários privilegiados", "A utilização de programas utilitários que podem ser capazes de ultrapassar controlos de sistema e aplicação é restrita e estritamente controlada?", order++);
            addControlItem(allItems, "9.13", "Controlo de acesso ao código fonte dos programas", "O acesso ao código fonte dos programas é restrito?", order++);

            // 10 - Criptografia
            addHeader(allItems, "10 - Criptografia", order++);
            addControlItem(allItems, "10.1", "Política para o uso de controlos criptográficos", "Uma política sobre o uso de controlos criptográficos para proteção da informação foi desenvolvida e implementada?", order++);
            addControlItem(allItems, "10.2", "Gestão de chaves", "Uma política sobre o uso, proteção e tempo de vida das chaves criptográficas está desenvolvida e implementada ao longo de todo o seu ciclo de vida?", order++);
            addControlItem(allItems, "10.3", "Criptografia de informações em trânsito", "As informações em trânsito são protegidas por meio de criptografia quando necessário com base na análise de risco?", order++);
            addControlItem(allItems, "10.4", "Criptografia de informações em repouso", "As informações em repouso são protegidas por meio de criptografia quando necessário com base na análise de risco?", order++);

            // 11 - Segurança física e ambiental
            addHeader(allItems, "11 - Segurança física e ambiental", order++);
            addControlItem(allItems, "11.1", "Perímetro de segurança física", "Perímetros de segurança são definidos e utilizados para proteger áreas que contêm informações sensíveis ou críticas e instalações de processamento de informação?", order++);
            addControlItem(allItems, "11.2", "Controlos de entrada física", "As áreas seguras são protegidas por controlos de entrada apropriados para assegurar que apenas pessoal autorizado tem acesso permitido?", order++);
            addControlItem(allItems, "11.3", "Segurança de escritórios, salas e instalações", "A segurança física para escritórios, salas e instalações é concebida e aplicada?", order++);
            addControlItem(allItems, "11.4", "Proteção contra ameaças ambientais", "É concebida e aplicada proteção física contra desastres naturais, ataques maliciosos ou acidentes?", order++);
            addControlItem(allItems, "11.5", "Trabalhar em áreas seguras", "São concebidos e aplicados procedimentos para trabalho em áreas seguras?", order++);
            addControlItem(allItems, "11.6", "Áreas de entrega e carregamento", "Os pontos de acesso como áreas de entrega e de carregamento e outros pontos onde pessoas não autorizadas podem entrar nas instalações são controlados?", order++);
            addControlItem(allItems, "11.7", "Instalação e proteção do equipamento", "O equipamento é corretamente instalado e protegido para reduzir os riscos de ameaças e perigos ambientais e oportunidades de acesso não autorizado?", order++);
            addControlItem(allItems, "11.8", "Serviços de suporte", "O equipamento está protegido contra falhas de energia e outras interrupções causadas por falhas nos serviços de suporte?", order++);
            addControlItem(allItems, "11.9", "Segurança do cabeamento", "O cabeamento de energia e de telecomunicações que transporta dados ou suporta serviços de informação está protegido contra interceptação, interferência ou danos?", order++);
            addControlItem(allItems, "11.10", "Manutenção do equipamento", "O equipamento é corretamente mantido para assegurar a sua disponibilidade e integridade contínuas?", order++);
            addControlItem(allItems, "11.11", "Remoção de ativos", "O equipamento, a informação ou o software não são retirados das instalações sem autorização prévia?", order++);
            addControlItem(allItems, "11.12", "Segurança do equipamento e ativos fora das instalações", "A segurança é aplicada a ativos fora das instalações, tendo em conta os diferentes riscos?", order++);
            addControlItem(allItems, "11.13", "Reutilização ou eliminação segura de equipamento", "Todos os itens de equipamento contendo mídia de armazenamento são verificados para garantir que qualquer dado sensível e software licenciado foi removido antes da eliminação ou reutilização?", order++);
            addControlItem(allItems, "11.14", "Equipamento de utilizador sem supervisão", "Os utilizadores asseguram-se que o equipamento sem supervisão tem proteção apropriada?", order++);
            addControlItem(allItems, "11.15", "Política de secretária limpa e ecrã limpo", "Uma política de secretária limpa para documentos e mídia de armazenamento removível e uma política de ecrã limpo para os sistemas de processamento de informação são adotadas?", order++);

            // 12 - Segurança nas operações
            addHeader(allItems, "12 - Segurança nas operações", order++);
            addControlItem(allItems, "12.1", "Procedimentos e responsabilidades operacionais", "Os procedimentos operacionais são documentados e disponibilizados a todos os utilizadores que deles necessitam?", order++);
            addControlItem(allItems, "12.2", "Gestão de mudanças", "As mudanças na organização, processos de negócio, sistemas e instalações de processamento de informação são controladas?", order++);
            addControlItem(allItems, "12.3", "Gestão de capacidade", "A utilização dos recursos é monitorizada, ajustada e são feitas projeções das futuras necessidades de capacidade?", order++);
            addControlItem(allItems, "12.4", "Separação dos ambientes de desenvolvimento, teste e produção", "Os ambientes de desenvolvimento, teste e produção são separados para reduzir os riscos de acesso ou alterações não autorizadas?", order++);
            addControlItem(allItems, "12.5", "Medidas contra software malicioso", "São implementados controlos de deteção, prevenção e recuperação, combinados com a conscientização dos utilizadores, para proteção contra software malicioso?", order++);
            addControlItem(allItems, "12.6", "Backup", "São efetuadas cópias de backup da informação, software e imagens do sistema e estas são testadas regularmente de acordo com a política de backup acordada?", order++);
            addControlItem(allItems, "12.7", "Registro e monitoramento", "Os logs de eventos são produzidos, armazenados, protegidos e analisados?", order++);
            addControlItem(allItems, "12.8", "Controlo de software operacional", "São implementados procedimentos para controlar a instalação de software em sistemas operacionais?", order++);
            addControlItem(allItems, "12.9", "Gestão de vulnerabilidades técnicas", "A informação sobre vulnerabilidades técnicas dos sistemas de informação é obtida e são tomadas medidas apropriadas?", order++);
            addControlItem(allItems, "12.10", "Restrições na instalação de software", "São estabelecidas e implementadas regras que regem a instalação de software pelos utilizadores?", order++);
            addControlItem(allItems, "12.11", "Auditoria de sistemas de informação", "Os requisitos e atividades de auditoria envolvendo verificação dos sistemas operacionais são cuidadosamente planeados e acordados para minimizar o risco de interrupções nos processos de negócio?", order++);

            // 13 - Segurança nas comunicações
            addHeader(allItems, "13 - Segurança nas comunicações", order++);
            addControlItem(allItems, "13.1", "Controlos de rede", "As redes são geridas e controladas para proteger a informação nos sistemas e aplicações?", order++);
            addControlItem(allItems, "13.2", "Segurança dos serviços de rede", "Os mecanismos de segurança, níveis de serviço e requisitos de gestão de todos os serviços de rede são identificados e incluídos nos acordos de serviço de rede?", order++);
            addControlItem(allItems, "13.3", "Segregação de redes", "Os grupos de serviços de informação, utilizadores e sistemas de informação são segregados em redes?", order++);
            addControlItem(allItems, "13.4", "Transferências de informação", "Existem acordos formais de transferência para proteger a informação transferida através do uso de todos os tipos de instalações de comunicação?", order++);
            addControlItem(allItems, "13.5", "Análise e especificação dos requisitos de segurança", "Os requisitos de segurança da informação são incluídos nos requisitos para novos sistemas?", order++);
            addControlItem(allItems, "13.6", "Segurança no desenvolvimento e suporte de processos", "As regras para o desenvolvimento de software e sistemas são estabelecidas e aplicadas aos desenvolvimentos dentro da organização?", order++);
            addControlItem(allItems, "13.7", "Dados para teste", "Os dados de teste são selecionados com cuidado, protegidos e controlados?", order++);

            // 14 - Aquisição, desenvolvimento e manutenção de sistemas
            addHeader(allItems, "14 - Aquisição, desenvolvimento e manutenção de sistemas", order++);
            addControlItem(allItems, "14.1", "Requisitos de segurança de sistemas de informação", "Os requisitos relacionados com segurança da informação são incluídos nos requisitos para novos sistemas ou melhorias?", order++);
            addControlItem(allItems, "14.2", "Segurança em processos de desenvolvimento e suporte", "As regras para o desenvolvimento de software e sistemas são estabelecidas e aplicadas aos desenvolvimentos na organização?", order++);
            addControlItem(allItems, "14.3", "Dados de teste", "Os dados de teste são selecionados, protegidos e controlados cuidadosamente?", order++);
            addControlItem(allItems, "14.4", "Desenvolvimento seguro", "Princípios de engenharia de sistemas seguros são estabelecidos, documentados, mantidos e aplicados a qualquer esforço de implementação de sistemas de informação?", order++);
            addControlItem(allItems, "14.5", "Ambiente de desenvolvimento seguro", "A organização estabelece e protege adequadamente os ambientes de desenvolvimento seguros para desenvolvimento de sistemas e esforços de integração?", order++);
            addControlItem(allItems, "14.6", "Desenvolvimento terceirizado", "A organização supervisiona e monitora a atividade de desenvolvimento de sistemas terceirizados?", order++);
            addControlItem(allItems, "14.7", "Teste de segurança do sistema", "Testes de funcionalidade de segurança são realizados durante o desenvolvimento?", order++);
            addControlItem(allItems, "14.8", "Teste de aceitação do sistema", "São estabelecidos programas de teste de aceitação e critérios relacionados para novos sistemas?", order++);

            // 15 - Relacionamento com fornecedores
            addHeader(allItems, "15 - Relacionamento com fornecedores", order++);
            addControlItem(allItems, "15.1", "Política de segurança da informação para o relacionamento com fornecedores", "Os requisitos de segurança da informação para mitigar os riscos associados ao acesso de fornecedores são acordados e documentados?", order++);
            addControlItem(allItems, "15.2", "Tratamento da segurança em acordos com fornecedores", "Todos os requisitos relevantes de segurança da informação são estabelecidos e acordados com cada fornecedor?", order++);
            addControlItem(allItems, "15.3", "Cadeia de fornecimento de ICT", "Os acordos com fornecedores incluem requisitos para abordar os riscos de segurança da informação associados a serviços de ICT e cadeia de fornecimento de produtos?", order++);
            addControlItem(allItems, "15.4", "Monitorização e revisão de serviços de fornecedores", "A organização monitora, revê e audita regularmente a entrega de serviços de fornecedores?", order++);
            addControlItem(allItems, "15.5", "Gestão de mudanças nos serviços de fornecedores", "As mudanças na provisão de serviços por fornecedores, incluindo manutenção e melhoria de políticas, procedimentos e controlos de segurança da informação existentes, são geridas?", order++);

            // 16 - Gestão de incidentes de segurança da informação
            addHeader(allItems, "16 - Gestão de incidentes de segurança da informação", order++);
            addControlItem(allItems, "16.1", "Responsabilidades e procedimentos", "As responsabilidades de gestão e procedimentos são estabelecidos para assegurar uma resposta rápida, efetiva e ordenada a incidentes de segurança da informação?", order++);
            addControlItem(allItems, "16.2", "Reportes de eventos de segurança da informação", "Os eventos de segurança da informação são reportados através dos canais de gestão apropriados o mais rapidamente possível?", order++);
            addControlItem(allItems, "16.3", "Reportes de pontos fracos de segurança da informação", "Os funcionários e contratados que utilizam os sistemas e serviços de informação da organização são obrigados a anotar e reportar quaisquer pontos fracos de segurança da informação observados ou suspeitos nos sistemas ou serviços?", order++);
            addControlItem(allItems, "16.4", "Avaliação e decisão sobre eventos de segurança da informação", "Os eventos de segurança da informação são avaliados e é decidido se devem ser classificados como incidentes de segurança da informação?", order++);
            addControlItem(allItems, "16.5", "Resposta a incidentes de segurança da informação", "A resposta a incidentes de segurança da informação está de acordo com os procedimentos documentados?", order++);
            addControlItem(allItems, "16.6", "Aprendizagem com os incidentes de segurança da informação", "O conhecimento adquirido a partir da análise e resolução de incidentes de segurança da informação é utilizado para reduzir a probabilidade ou o impacto de incidentes futuros?", order++);
            addControlItem(allItems, "16.7", "Recolha de evidências", "A organização define e aplica procedimentos para a identificação, recolha, aquisição e preservação de informações que podem servir como evidência?", order++);

            // 17 - Aspectos de segurança da informação na gestão da continuidade do negócio
            addHeader(allItems, "17 - Aspectos de segurança da informação na gestão da continuidade do negócio", order++);
            addControlItem(allItems, "17.1", "Planeamento da continuidade da segurança da informação", "A organização determina os seus requisitos para a segurança da informação e a continuidade da gestão da segurança da informação em situações adversas?", order++);
            addControlItem(allItems, "17.2", "Implementação da continuidade da segurança da informação", "A organização estabelece, documenta, implementa e mantém processos, procedimentos e controlos para assegurar o nível necessário de continuidade para a segurança da informação durante uma situação adversa?", order++);
            addControlItem(allItems, "17.3", "Verificação, revisão e avaliação da continuidade da segurança da informação", "A organização verifica os controlos de continuidade da segurança da informação em intervalos regulares para garantir que são válidos e eficazes durante situações adversas?", order++);
            addControlItem(allItems, "17.4", "Disponibilidade das instalações de processamento de informação", "As instalações de processamento de informação são implementadas com redundância suficiente para atender aos requisitos de disponibilidade?", order++);
            
            // 18 - Conformidade
            addHeader(allItems, "18 - Conformidade", order++);
            addControlItem(allItems, "18.1", "Identificação da legislação aplicável e de requisitos contratuais", "Todos os requisitos legislativos estatutários, regulamentares e contratuais relevantes são explicitamente identificados, documentados e mantidos atualizados para cada sistema de informação da organização?", order++);
            addControlItem(allItems, "18.2", "Direitos de propriedade intelectual", "Procedimentos apropriados são implementados para garantir a conformidade com requisitos legislativos, regulamentares e contratuais relacionados com direitos de propriedade intelectual e uso de produtos de software proprietários?", order++);
            addControlItem(allItems, "18.3", "Proteção de registros", "Os registros são protegidos contra perda, destruição, falsificação, acesso não autorizado e divulgação não autorizada, de acordo com requisitos legislativos, regulamentares, contratuais e de negócio?", order++);
            addControlItem(allItems, "18.4", "Privacidade e proteção de dados pessoais", "A privacidade e a proteção de dados pessoais são asseguradas conforme exigido na legislação e regulamentação relevantes aplicáveis?", order++);
            addControlItem(allItems, "18.5", "Regulamentação de controlos criptográficos", "Os controlos criptográficos são usados em conformidade com todos os acordos, legislação e regulamentações relevantes?", order++);
            addControlItem(allItems, "18.6", "Revisão independente da segurança da informação", "A abordagem da organização para gerir a segurança da informação e a sua implementação é revista independentemente em intervalos planeados ou quando ocorrerem mudanças significativas?", order++);
            addControlItem(allItems, "18.7", "Conformidade com as políticas e normas de segurança", "Os gestores reveem regularmente a conformidade do processamento da informação e procedimentos dentro da sua área de responsabilidade com as políticas, normas e outros requisitos de segurança apropriados?", order++);
            addControlItem(allItems, "18.8", "Revisão técnica da conformidade", "Os sistemas de informação são revistos regularmente para verificar a conformidade com as políticas e normas de segurança da informação da organização?", order++);
 
            checklistRepository.saveAll(allItems);
        }
    }
    // Método auxiliar para adicionar cabeçalho
    private void addHeader(List<Checklist> items, String titulo, int ordem) {
        Checklist header = new Checklist();
        header.setTarefas(titulo);
        header.setIsHeader(true);
        header.setDisplayorder(ordem);
        header.setEmConformidade(false); // Inicializa para evitar erros de null
        items.add(header);
    }
    // Método auxiliar para adicionar controlo
    private void addControlItem(List<Checklist> items, String controlos, String faseImplementacao, String tarefas, int ordem) {
        Checklist item = new Checklist();
        item.setControlos(controlos);
        item.setFase_implementacao(faseImplementacao);
        item.setTarefas(tarefas);
        item.setEmConformidade(false);
        item.setIsHeader(false);
        item.setDisplayorder(ordem);
        items.add(item);
    }
}