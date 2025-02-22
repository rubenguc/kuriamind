import {useCustomToast} from '@/hooks';
import {Block} from '@/interfaces';
import {saveBlock, updateBlock} from '@/native-modules';
import {zodResolver} from '@hookform/resolvers/zod';
import {useForm} from 'react-hook-form';
import {useTranslation} from 'react-i18next';
import z from 'zod';

const blockToSaveSchema = z
  .object({
    name: z.string().min(1, 'name_error'),
    blockedApps: z.array(z.string()).nonempty('blocked_apps_error'),
    blockApps: z.boolean(),
    blockNotifications: z.boolean(),
  })
  .refine(data => data.blockApps || data.blockNotifications, {
    message: 'options_error',
    path: ['blockApps'],
  });

type blockToSaveSchemaType = z.infer<typeof blockToSaveSchema>;

const DEFAULT_VALUES = {
  name: '',
  blockedApps: [],
  blockApps: true,
  blockNotifications: true,
};

interface useBlockProps {
  defaultBlock: Block | undefined;
  onFinishSubmit: () => void;
}

export const useBlock = ({defaultBlock, onFinishSubmit}: useBlockProps) => {
  const {t} = useTranslation('block');
  const {showSuccessToast, showErrorToast} = useCustomToast();

  const {
    control,
    handleSubmit,
    reset,
    formState: {errors},
  } = useForm<blockToSaveSchemaType>({
    defaultValues: defaultBlock
      ? {
          name: defaultBlock.name,
          blockedApps: defaultBlock.blockedApps,
          blockApps: defaultBlock.blockApps,
          blockNotifications: defaultBlock.blockNotifications,
        }
      : DEFAULT_VALUES,
    resolver: zodResolver(blockToSaveSchema),
  });

  const onSubmit = handleSubmit(async (data: blockToSaveSchemaType) => {
    try {
      if (isEditing) {
        await updateBlock({
          ...data,
          id: defaultBlock!.id,
          isActive: defaultBlock!.isActive,
        });
      } else {
        await saveBlock(data);
      }

      showSuccessToast({
        description: t(
          isEditing ? 'block_updated_successfully' : 'block_saved_successfully',
        ),
      });
      reset(DEFAULT_VALUES);
      onFinishSubmit();
    } catch (error) {
      showErrorToast({
        description: isEditing ? 'block_updated_error' : 'block_saved_error',
      });
    }
  });

  const isEditing = !!defaultBlock;

  return {
    control,
    onSubmit,
    handleSubmit,
    errors,
    isEditing,
  };
};
