import {useCustomToast} from '@/hooks';
import {Block, BlockToSave} from '@/interfaces';
import {saveBlock, updateBlock} from '@/native-modules';
import {zodResolver} from '@hookform/resolvers/zod';
import {useForm} from 'react-hook-form';
import {useTranslation} from 'react-i18next';
import z from 'zod';
import {format} from 'date-fns';

const blockToSaveSchema = z
  .object({
    name: z.string().min(1, 'name_error'),
    blockedApps: z.array(z.string()).nonempty('blocked_apps_error'),
    blockApps: z.boolean(),
    blockNotifications: z.boolean(),
    addTimer: z.boolean(),
    startTime: z.string(),
    endTime: z.string(),
  })
  .refine(data => data.blockApps || data.blockNotifications, {
    message: 'options_error',
    path: ['blockApps'],
  })
  .superRefine((data, ctx) => {
    if (data.addTimer) {
      if (!data.startTime || !data.endTime) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'time_error',
          path: ['endTime'],
        });
      } else if (data.endTime <= data.startTime) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'time_error',
          path: ['endTime'],
        });
      }
    }
  });

type blockToSaveSchemaType = z.infer<typeof blockToSaveSchema>;

const DEFAULT_VALUES: BlockToSave = {
  name: '',
  blockedApps: [],
  blockApps: true,
  blockNotifications: true,
  startTime: '',
  endTime: '',
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
    watch,
  } = useForm<blockToSaveSchemaType>({
    defaultValues: defaultBlock
      ? {
          name: defaultBlock.name,
          blockedApps: defaultBlock.blockedApps,
          blockApps: defaultBlock.blockApps,
          blockNotifications: defaultBlock.blockNotifications,
          addTimer:
            defaultBlock.startTime !== '' && defaultBlock.endTime !== '',
          startTime: defaultBlock.startTime,
          endTime: defaultBlock.endTime,
        }
      : {...DEFAULT_VALUES, addTimer: false},
    resolver: zodResolver(blockToSaveSchema),
  });

  const onSubmit = handleSubmit(
    async ({addTimer, ...data}: blockToSaveSchemaType) => {
      try {
        const _block = {
          ...data,
          startTime: addTimer ? data.startTime : '',
          endTime: addTimer ? data.endTime : '',
        };

        if (isEditing) {
          await updateBlock({
            ..._block,
            id: defaultBlock!.id,
            isActive: defaultBlock!.isActive,
          });
        } else {
          await saveBlock({
            ..._block,
          });
        }

        showSuccessToast({
          description: t(
            isEditing
              ? 'block_updated_successfully'
              : 'block_saved_successfully',
          ),
        });
        reset(DEFAULT_VALUES);
        onFinishSubmit();
      } catch (error) {
        showErrorToast({
          description: t(
            isEditing ? 'block_updated_error' : 'block_saved_error',
          ),
        });
      }
    },
  );

  const formatTime = (time: string) => {
    return format(time, 'HH:mm');
  };

  const isAddTimerActive = watch('addTimer');

  const isEditing = !!defaultBlock;

  return {
    control,
    onSubmit,
    handleSubmit,
    errors,
    isEditing,
    formatTime,
    isAddTimerActive,
  };
};
