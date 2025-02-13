import {useCustomToast} from '@/hooks';
import {saveBlock} from '@/native-modules';
import {zodResolver} from '@hookform/resolvers/zod';
import {useForm, Controller} from 'react-hook-form';
import z from 'zod';

const blockToSaveSchema = z.object({
  name: z.string().min(1, 'El nombre es obligatorio'),
  blockedApps: z
    .array(z.string())
    .nonempty('Debe haber al menos una app bloqueada'),
  blockApps: z.boolean(),
  blockNotifications: z.boolean(),
});

type blockToSaveSchemaType = z.infer<typeof blockToSaveSchema>;

const DEFAULT_VALUES = {
  name: '',
  blockedApps: [],
  blockApps: true,
  blockNotifications: true,
};

export const useBlock = () => {
  const {showSuccessToast, showErrorToast} = useCustomToast();

  const {
    control,
    handleSubmit,
    reset,
    formState: {errors},
  } = useForm<blockToSaveSchemaType>({
    defaultValues: DEFAULT_VALUES,
    resolver: zodResolver(blockToSaveSchema),
  });

  const onSubmit = handleSubmit(async (data: blockToSaveSchemaType) => {
    try {
      await saveBlock(data);
      showSuccessToast({
        description: 'El bloque se ha creado correctamente',
      });
      reset(DEFAULT_VALUES);
    } catch (error) {
      showErrorToast({
        description: 'Error al crear el bloque',
      });
    }
  });

  return {
    control,
    onSubmit,
    handleSubmit,
    errors,
  };
};
